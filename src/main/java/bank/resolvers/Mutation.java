package bank.resolvers;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
public class Mutation implements GraphQLMutationResolver {

	@Autowired
	private Bank bank;

	/* errors according https://github.com/mikenoethiger/bank-server#status-codes */
	private static final Response ERROR_ACCOUNT_DOES_NOT_EXIST = new Response(1, toCollection("Account does not exist."));
	private static final Response ERROR_ACCOUNT_COULD_NOT_BE_CREATED = new Response(2, toCollection("Account could not be created."));
	private static final Response ERROR_ACCOUNT_COULD_NOT_BE_CLOSED = new Response(3, toCollection("Account could not be closed."));
	private static final Response ERROR_INACTIVE_ACCOUNT = new Response(4, toCollection("Inactive account."));
	private static final Response ERROR_ACCOUNT_OVERDRAW = new Response(5, toCollection("Account overdraw."));
	private static final Response ERROR_ILLEGAL_ARGUMENT = new Response(6, toCollection("Illegal argument."));
	// XXX die beiden folgenden werden nicht verwendet.
	private static final Response ERROR_BAD_REQUEST = new Response(7, toCollection("Bad request."));
	private static final Response ERROR_INTERNAL_ERROR = new Response(8, toCollection("Internal error."));

	public Response createAccount(@NotNull String owner) throws IOException {
		String number;

		try {
			number = bank.createAccount(owner);
		} catch (IOException e) {
			return ERROR_ACCOUNT_COULD_NOT_BE_CREATED;
		}
		Account acc = bank.getAccount(number);

		String[] data = new String[] { acc.getNumber(), acc.getOwner(), String.valueOf(acc.getBalance()), acc.isActive() ? "1" : "0" };

		return new Response(0, toCollection(data));
	}

	public Response deposit(@NotNull String accountNumber, @NotNull float amount) throws IOException {
		Account acc = bank.getAccount(accountNumber);
		if (acc == null) return ERROR_ACCOUNT_DOES_NOT_EXIST;

		try {
			acc.deposit(amount);
		} catch (InactiveException e) {
			return ERROR_INACTIVE_ACCOUNT;
		} catch (IllegalArgumentException e) {
			return ERROR_ILLEGAL_ARGUMENT;
		}

		return new Response(0, toCollection(String.valueOf(acc.getBalance())));
	}

	public Response withdraw(@NotNull String accountNumber, @NotNull float amount) throws IOException {
		Account acc = bank.getAccount(accountNumber);
		if (acc == null) return ERROR_ACCOUNT_DOES_NOT_EXIST;

		try {
			acc.withdraw(amount);
		} catch (InactiveException e) {
			return ERROR_INACTIVE_ACCOUNT;
		} catch (IllegalArgumentException e) {
			return ERROR_ILLEGAL_ARGUMENT;
		} catch (OverdrawException e) {
			return ERROR_ACCOUNT_OVERDRAW;
		}

		return new Response(0, toCollection(String.valueOf(acc.getBalance())));
	}

	public Response transfer(@NotNull String accountNumberSender, @NotNull String accountNumberReceiver, @NotNull float amount) throws IOException {
		Account sender = bank.getAccount(accountNumberSender);
		Account receiver = bank.getAccount(accountNumberReceiver);
		if (sender == null || receiver == null) return ERROR_ACCOUNT_DOES_NOT_EXIST;

		try {
			bank.transfer(sender, receiver, amount);
		} catch (OverdrawException e) {
			return ERROR_ACCOUNT_OVERDRAW;
		} catch (InactiveException e) {
			return ERROR_INACTIVE_ACCOUNT;
		} catch (IllegalArgumentException e) {
			return ERROR_ILLEGAL_ARGUMENT;
		}

		return new Response(0, toCollection(String.valueOf(sender.getBalance()), String.valueOf(receiver.getBalance())));
	}

	public Response closeAccount(@NotNull String accountNumber) throws IOException {
		Account acc = bank.getAccount(accountNumber);
		if (acc == null) return ERROR_ACCOUNT_DOES_NOT_EXIST;

		boolean res = bank.closeAccount(accountNumber);
		if (!res) return ERROR_ACCOUNT_COULD_NOT_BE_CLOSED;

		return new Response(0, new ArrayList<>());
	}

	private static Collection<String> toCollection(String... str) {
		return new ArrayList<>(Arrays.asList(str));
		// XXX ist das new ArrayList<> rund um Arrays.asList nötig?
		//     Arrays.asList ist ja bereits eine Liste.
	}

	private static class Response {
		private final int statusCode;
		private final Collection<String> data;

		public Response(int statusCode, Collection<String> data) {
			this.statusCode = statusCode;
			this.data = data;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public Collection<String> getData() {
			return data;
		}
	}
}
