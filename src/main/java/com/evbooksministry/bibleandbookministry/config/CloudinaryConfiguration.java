package com.evbooksministry.bibleandbookministry.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfiguration {
    private final String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
    private final String cloudAPIKey = System.getenv("CLOUDINARY_CLOUD_KEY");
    private final String cloudAPISecret = System.getenv("CLOUDINARY_CLOUD_SECRET");


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudAPIKey,
                "api_secret", cloudAPISecret
        ));
    }
}
