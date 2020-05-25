package bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		// the following property definition is only necessary as this project contains two spring boot applications
		SpringApplication.run(BankApplication.class, args);
	}

}