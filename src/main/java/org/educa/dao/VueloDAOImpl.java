package org.educa.dao;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.entity.VueloWithRelations;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.BeneficioVuelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase que define los metodos implementados por la interfaz VueloDAO
 *
 * @author Joel Murillo Masa
 */

public class VueloDAOImpl implements VueloDAO {
    private static final String COLLECTION_VUELOS = "vuelos";
    private static final String COLLECTION_RESERVAS = "reservas";
    private final Gson gson = new GsonBuilder().create();

    /**
     * Obtiene los beneficios de los vuelos
     *
     * @return Se devuelve una lista con los beneficios obtenidos
     */
    @Override
    public List<BeneficioVuelo> getBeneficioVuelo() {
        List<VueloWithRelations> vueloWithRelations = new ArrayList<>();
        List<BeneficioVuelo> beneficioVuelos = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());

            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_VUELOS);
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.lookup("aeropuertos", "origen_id", "_id", "origen"),
                    Aggregates.lookup("aeropuertos", "destino_id", "_id", "destino"),
                    Aggregates.unwind("$origen"),
                    Aggregates.unwind("$destino")
            );
            AggregateIterable<Document> aggregateIterable = collection.aggregate(pipeline);
            for (Document document : aggregateIterable) {
                vueloWithRelations.add(gson.fromJson(document.toJson(), VueloWithRelations.class));
            }
            for (VueloWithRelations vuelo : vueloWithRelations) {
                BeneficioVuelo beneficioVuelo = new BeneficioVuelo();
                beneficioVuelo.setCodigoVuelo(vuelo.getCodigoVuelo());
                beneficioVuelo.setCoste(vuelo.getCoste());
                beneficioVuelo.setOrigen(vuelo.getOrigen().getNombre());
                beneficioVuelo.setDestino(vuelo.getDestino().getNombre());
                //Encontrar el total de pasajeros de cada vuelo
                beneficioVuelo.setNumPasajeros(findPasajeros(vuelo.getId()));
                beneficioVuelos.add(beneficioVuelo);
            }

            return beneficioVuelos;

        }
    }

    /**
     * Busca todas las reservas
     *
     * @return Se devuelve una lista con todas las reservas
     */
    @Override
    public List<ReservaWithRelations> findAll() {
        List<ReservaWithRelations> reservas = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.lookup("vuelos", "vuelo_id", "_id", "vuelo"),
                    Aggregates.unwind("$vuelo")
            );
            AggregateIterable<Document> aggregateIterable = collection.aggregate(pipeline);
            for (Document document : aggregateIterable) {
                reservas.add(gson.fromJson(document.toJson(), ReservaWithRelations.class));
            }
        }
        return reservas;
    }

    /**
     * Busca cuantos pasajeros hay con el id de ese vuelo
     *
     * @param id ID del vuelo a buscar
     * @return Se devuelve el numero de pasajeros que tiene ese vuelo
     */
    private Integer findPasajeros(Integer id) {
        List<ReservaEntity> reservaEntityList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            FindIterable<Document> findIterable = mongoCollection.find(Filters.eq("vuelo_id", id));
            for (Document document : findIterable) {
                reservaEntityList.add(gson.fromJson(document.toJson(), ReservaEntity.class));
            }
            return reservaEntityList.size();
        }
    }
}

