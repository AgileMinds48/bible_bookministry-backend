package com.evbooksministry.bibleandbookministry.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {

    Dotenv dotenv = Dotenv.configure().load();
    private final String cloudName = dotenv.get("CLOUDINARY_CLOUD_NAME");
    private final String cloudAPIKey = dotenv.get("CLOUDINARY_CLOUD_KEY");
    private final String cloudAPISecret = dotenv.get("CLOUDINARY_CLOUD_SECRET");


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudAPIKey,
                "api_secret", cloudAPISecret
        ));
    }
}
