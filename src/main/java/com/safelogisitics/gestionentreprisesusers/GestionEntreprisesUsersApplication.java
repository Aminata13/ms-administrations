package com.safelogisitics.gestionentreprisesusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class GestionEntreprisesUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEntreprisesUsersApplication.class, args);
	}
}
