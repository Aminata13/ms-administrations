package com.safelogisitics.gestionentreprisesusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(exclude = {
	MongoAutoConfiguration.class,
	MongoDataAutoConfiguration.class
})@EnableMongoAuditing
public class GestionEntreprisesUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionEntreprisesUsersApplication.class, args);
	}
}
