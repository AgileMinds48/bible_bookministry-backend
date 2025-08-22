package com.evbooksministry.bibleandbookministry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibleAndBookMinistryApplication {

	public static void main(String[] args) {
        System.setProperty("MAIL_HOST", System.getenv("MAIL_HOST"));
		System.setProperty("MAIL_PORT", System.getenv("MAIL_PORT"));
		System.setProperty("MAIL_USERNAME", System.getenv("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));

		//PostgreSQL database
		System.setProperty("DB_URL", System.getenv("DB_URL"));
		System.setProperty("DB_USERNAME", System.getenv("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));

		System.setProperty("CLOUDINARY_CLOUD_NAME", System.getenv("CLOUDINARY_CLOUD_NAME"));
		System.setProperty("CLOUDINARY_CLOUD_SECRET", System.getenv("CLOUDINARY_CLOUD_SECRET"));
		System.setProperty("CLOUDINARY_CLOUD_KEY", System.getenv("CLOUDINARY_CLOUD_KEY"));


		System.setProperty("REDIS_URL", System.getenv("REDIS_URL"));

		SpringApplication.run(BibleAndBookMinistryApplication.class, args);
	}

}
