package com.CeyBazaar.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("http://localhost:3000,http://ceybazaar.s3-website.eu-north-1.amazonaws.com/") // Allow requests from localhost:3000
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials (e.g., cookies)
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Map /uploads/** to the directory C:/uploads
//                registry.addResourceHandler("/uploads/**")
//                        .addResourceLocations("file:C:/uploads/");
                registry.addResourceHandler("/home/ec2-user/uploads/**")
                        .addResourceLocations("file:/home/ec2-user/uploads/");
            }
        };
    }
}
