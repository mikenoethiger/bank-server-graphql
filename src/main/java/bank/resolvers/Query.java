package bank.resolvers;

import bank.Account;
import bank.Bank;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
public class Query implements GraphQLQueryResolver {

	private Bank bank;

	@Autowired
	public Query(Bank bank) throws IOException {
		this.bank = bank;
	}

	public Collection<String> accounts() throws IOException {
		return bank.getAccountNumbers();
	}

	public Optional<Account> account(String number) throws IOException {
		return Optional.ofNullable(bank.getAccount(number));
	}
}
