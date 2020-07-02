package com.CAT.BuffetAPI;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuffetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuffetApiApplication.class, args);
	}

}
