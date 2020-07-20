package com.CAT.BuffetAPI;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//Esta es la funcion principal de la API y que inicia el proceso.
//Se ha añadido unicamente la funcion EnableScheduling para permitir el uso de tareas programadas.


@SpringBootApplication
@EnableScheduling
public class BuffetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuffetApiApplication.class, args);
	}

}
