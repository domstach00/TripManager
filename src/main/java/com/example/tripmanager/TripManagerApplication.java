package com.example.tripmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.tripmanager")
public class TripManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripManagerApplication.class, args);
    }

}
