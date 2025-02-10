package org.educa.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoWriteException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;

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
        } catch (MongoWriteException e) {
            throw new RuntimeException("Error, el id de la reserva ya esta siendo usado");
        }
    }

    @Override
    public List<ReservaEntity> findAll() {
        List<ReservaEntity> reservas = new ArrayList<>();
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);
            FindIterable<Document> findIterable = mongoCollection.find();
            for (Document doc : findIterable) {
                reservas.add(gson.fromJson(doc.toJson(), ReservaEntity.class));
            }
            return reservas;
        }
    }

    @Override
    public ReservaEntity findById(int id) {
        try(MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION);
            Document doc= mongoCollection.find(eq("_id",id)).first();
            if (doc != null) {
                return gson.fromJson(doc.toJson(), ReservaEntity.class);
            } else {
                return null;
            }
        }
    }

    @Override
    public Long update(ReservaEntity reservaToUpdate) {
        try(MongoClient mongoClient= MongoClients.create(DatabaseSettings.getURL())){
            MongoDatabase mongoDatabase= mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection= mongoDatabase.getCollection(COLLECTION);
            Bson filter= Filters.eq("_id",reservaToUpdate.getId());
            List<Bson> lista= Arrays.asList(
                    Updates.set("precio",reservaToUpdate.getPrecio()),
                    Updates.set("asiento",reservaToUpdate.getAsiento()),
                    Updates.set("estado",reservaToUpdate.getEstado())

            );
            return mongoCollection.updateOne(filter,lista).getModifiedCount();
        }

    }

    @Override
    public Long delete(int id) {
        return 0L;
    }

    @Override
    public List<ReservaEntity> findReservasByVueloId(Integer vueloId) {
        return List.of();
    }

    @Override
    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        return null;
    }

    @Override
    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        return List.of();
    }
}
