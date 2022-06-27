package com.shop.projectlion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication()
@PropertySource("classpath:/env.properties")
public class ProjectLionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectLionApplication.class, args);
    }

}
