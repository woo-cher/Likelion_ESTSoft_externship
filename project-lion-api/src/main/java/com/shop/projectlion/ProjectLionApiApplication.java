package com.shop.projectlion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("env.properties")
public class ProjectLionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectLionApiApplication.class, args);
    }

}
