package com.annotations.infra;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

@ApplicationScoped
public class MongoConnection implements MongoConfig {

    public static final String MONGO_LOCALHOST = "mongodb://localhost:27017";
    public static final String DATABASE = "admin";
    public static final String COLLECTION = "teste";

    @Override
    public MongoCollection<Document> getCollection() {
        return getMongoClient()
                .getDatabase(DATABASE)
                .getCollection(COLLECTION);
    }

    @Override
    public MongoClient getMongoClient() {
        return MongoClients.create(MONGO_LOCALHOST);
    }
}