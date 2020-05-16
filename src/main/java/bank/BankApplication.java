package bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		// the following property definition is only necessary as this project contains two spring boot applications
		// XXX das ist bei ihnen nicht mehr der Fall, d.h. sie können die folgende Zeile löschen.
		System.setProperty("graphql.tools.schemaLocationPattern", "**/bank.graphqls");
		SpringApplication.run(BankApplication.class, args);
	}

}