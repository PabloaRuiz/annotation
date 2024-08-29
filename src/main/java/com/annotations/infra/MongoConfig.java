package com.annotations.infra;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public interface MongoConfig {

     MongoCollection<Document> getCollection();

     MongoClient getMongoClient();
}
