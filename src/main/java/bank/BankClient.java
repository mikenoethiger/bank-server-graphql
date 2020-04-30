package bank;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;

import bank.resolvers.AccountGson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class BankClient {
	
	static String query1 = "query {" + 
			"  account(number: \"CH5610000000000000000\") {" +
			"    number" +
			"    owner" +
			"    balance" +
			"    active" +
			"  }" + 
			"}";

	public static void main(String[] args) throws Exception {
		HttpClient client = HttpClient.newHttpClient();
		
		ObjectMapper objectMapper = new ObjectMapper();
	    String requestBody = objectMapper
	          .writerWithDefaultPrettyPrinter()
	          .writeValueAsString(new Query(query1));
	    
		BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI("http://localhost:5002/graphql"))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.POST(body)
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("Statuscode: " + response.statusCode());
		System.out.println("Headers:");
		response.headers().map().forEach((k,v) -> System.out.println(k + ": " + v));
		System.out.println("Body:");
		System.out.println(response.body());
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
		JsonObject jsonAccount = jsonObject.getAsJsonObject("data").getAsJsonObject("account");
		System.out.println(jsonAccount );
		System.out.println(gson.fromJson(jsonAccount, AccountGson.class));
//		System.out.println("\n"+gson.toJson(gson.fromJson(response.body(), Object.class)));
	}
	
	static class Query {
		private final String query;
		private final String variables;

		public Query(String query, String variables) {
			this.query = query;
			this.variables = variables;
		}

		public Query(String query) {
			this(query, null);
		}

		public String getQuery() {
			return query;
		}

		public String getVariables() {
			return variables;
		}
	}

}
