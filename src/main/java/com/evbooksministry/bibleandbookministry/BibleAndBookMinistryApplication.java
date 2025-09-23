package com.evbooksministry.bibleandbookministry;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibleAndBookMinistryApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv
                .configure()
                .load();


		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));

		//PostgreSQL database
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME"));
		System.setProperty("CLOUDINARY_CLOUD_SECRET", dotenv.get("CLOUDINARY_CLOUD_SECRET"));
		System.setProperty("CLOUDINARY_CLOUD_KEY", dotenv.get("CLOUDINARY_CLOUD_KEY"));


		System.setProperty("REDIS_URL", dotenv.get("REDIS_URL"));

		SpringApplication.run(BibleAndBookMinistryApplication.class, args);
	}

}
