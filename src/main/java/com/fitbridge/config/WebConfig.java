package com.fitbridge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir");
        String uploadDir = Paths.get(projectRoot, "public").toFile().getAbsolutePath();

        registry.addResourceHandler("/exercises/**")
                .addResourceLocations("file:" + uploadDir + "/exercises/");

        registry.addResourceHandler("/workouts/**")
                .addResourceLocations("file:" + uploadDir + "/workouts/");
    }
}
