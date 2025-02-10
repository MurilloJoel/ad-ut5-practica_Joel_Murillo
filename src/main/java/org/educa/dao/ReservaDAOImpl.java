package org.educa.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.educa.entity.ReservaEntity;
import org.educa.settings.DatabaseSettings;

public class ReservaDAOImpl implements ReservaDAO {
    private static final String COLLECTION= "reservas";
    private final Gson gson = new GsonBuilder().create();
    @Override
    public Integer save(ReservaEntity reserva) {
        try (MongoClient mongoClient= MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase= mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection= mongoDatabase.getCollection(COLLECTION);
            String json = gson.toJson(reserva);
            InsertOneResult ior = mongoCollection.insertOne(Document.parse(json));
            if (ior.getInsertedId()!=null) {
                return ior.getInsertedId().asInt32().intValue();
            } else {
                return 0;
            }
        }
    }
}
