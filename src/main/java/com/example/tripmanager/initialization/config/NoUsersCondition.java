package com.example.tripmanager.initialization.config;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.shared.model.AbstractEntity;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Collections;
import java.util.Objects;

public class NoUsersCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();

        String host = env.getProperty("spring.data.mongodb.host", "localhost");
        int port = Integer.parseInt(env.getProperty("spring.data.mongodb.port", "27017"));
        String dbName = env.getProperty("spring.data.mongodb.database");
        Objects.requireNonNull(dbName);
        String username = env.getProperty("spring.data.mongodb.username");
        String password = env.getProperty("spring.data.mongodb.password");
        String authDb  = env.getProperty("spring.data.mongodb.authentication-database", dbName);

        if (StringUtils.isBlank(dbName)) {
            return false;
        }

        MongoClientSettings.Builder builder = MongoClientSettings.builder()
                .applyToClusterSettings(cs -> cs.hosts(Collections.singletonList(new ServerAddress(host, port))));

        if (username != null && !username.isBlank() && password != null) {
            MongoCredential cred = MongoCredential.createCredential(
                    username,
                    (StringUtils.isBlank(authDb)) ? dbName : authDb,
                    password.toCharArray()
            );
            builder.credential(cred);
        }

        MongoClientSettings settings = builder.build();

        try (MongoClient client = MongoClients.create(settings)) {
            MongoDatabase db = client.getDatabase(dbName);
            Document first = db.getCollection(Account.COLLECTION_NAME)
                    .find()
                    .projection(Projections.include(AbstractEntity.FIELD_NAME_ID))
                    .limit(1)
                    .first();
            return first == null;
        } catch (Exception e) {
            return false;
        }
    }
}
