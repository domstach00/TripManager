package com.example.tripmanager.initialization.shutdowner;

import com.mongodb.client.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppShutdowner {
    private static final Logger log = LoggerFactory.getLogger(AppShutdowner.class);
    private static final Long PRE_SHUTDOWN_DELAY_MS = 5000L;

    private final ConfigurableApplicationContext ctx;
    private final MongoClient mongoClient;

    public AppShutdowner(ConfigurableApplicationContext ctx, MongoClient mongoClient) {
        this.ctx = ctx;
        this.mongoClient = mongoClient;
    }

    public void shutdown() {
        new Thread(() -> {
            try {
                Thread.sleep(PRE_SHUTDOWN_DELAY_MS);
            } catch (InterruptedException ignored) {}

            try {
                mongoClient.close();
            } catch (Exception e) {
                log.error("Error while closing mongo client, ERROR={}", e.getMessage());
            }

            int code = org.springframework.boot.SpringApplication.exit(ctx, () -> 0);
            log.info("Shutting down application with exit code={}", code);
            System.exit(code);
        }, "tm-shutdowner-thread").start();
    }
}
