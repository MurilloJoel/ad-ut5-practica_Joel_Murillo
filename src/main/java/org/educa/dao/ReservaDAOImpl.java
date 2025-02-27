package org.educa.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoWriteException;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.educa.entity.PasajeroEntity;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.InfoPasajero;
import org.educa.wrappers.VueloWithPrecio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Clase que define los metodos implementados por la interfaz ReservaDAO
 *
 * @author Joel Murillo Masa
 */

public class ReservaDAOImpl implements ReservaDAO {
    private static final String COLLECTION_RESERVAS = "reservas";
    private static final String COLLECTION_PASAJEROS = "pasajeros";
    private final Gson gson = new GsonBuilder().create();

    /**
     * Guarda la reserva y devuelve el id insertado
     *
     * @param reserva Reserva a insertar
     * @return Se devuelve el id insertado
     */
    @Override
    public Integer save(ReservaEntity reserva) {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            String json = gson.toJson(reserva);
            InsertOneResult ior = mongoCollection.insertOne(Document.parse(json));
            if (ior.getInsertedId() != null) {
                return ior.getInsertedId().asInt32().intValue();
            } else {
                return 0;
            }
        } catch (MongoWriteException e) {
            throw new RuntimeException("Error, el id de la reserva ya esta siendo usado");
        }
    }

    /**
     * Busca todas las reservas
     *
     * @return Se devuelve una lista con todas las reservas
     */
    @Override
    public List<ReservaEntity> findAll() {
        List<ReservaEntity> reservas = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            FindIterable<Document> findIterable = mongoCollection.find();
            for (Document doc : findIterable) {
                reservas.add(gson.fromJson(doc.toJson(), ReservaEntity.class));
            }
            return reservas;
        }
    }

    /**
     * Busca una reserva por el id
     *
     * @param id ID de la reserva a buscar
     * @return Se devuelve una reserva.
     */
    @Override
    public ReservaEntity findById(int id) {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            Document doc = mongoCollection.find(eq("_id", id)).first();
            if (doc != null) {
                return gson.fromJson(doc.toJson(), ReservaEntity.class);
            } else {
                return null;
            }
        }
    }

    /**
     * Modifica la reserva introducida por parametro y devuelve el numero de reservas modificadas
     *
     * @param reservaToUpdate Reserva a modificar
     * @return Se devuelve el numero de reservas modificadas
     */
    @Override
    public Long update(ReservaEntity reservaToUpdate) {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            Bson filter = eq("_id", reservaToUpdate.getId());
            List<Bson> lista = Arrays.asList(
                    Updates.set("precio", reservaToUpdate.getPrecio().doubleValue()),
                    Updates.set("asiento", reservaToUpdate.getAsiento()),
                    Updates.set("estado", reservaToUpdate.getEstado())

            );
            return mongoCollection.updateOne(filter, lista).getModifiedCount();
        }

    }

    /**
     * Borra la reserva correspondiente con el id especificado
     *
     * @param id ID de la reserva a eliminar
     * @return Se devuelve el numero de reservas eliminadas
     */
    @Override
    public Long delete(int id) {
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            Bson filter = eq("_id", id);

            return mongoCollection.deleteOne(filter).getDeletedCount();
        }
    }



    /**
     * Busca la reserva a traves del pasaporte
     *
     * @param pasaporte Pasaporte a buscar
     * @return Se devuelve la informacion del pasajero
     */
    @Override
    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        List<ReservaWithRelations> reservas = new ArrayList<>();
        List<VueloWithPrecio> vuelos = new ArrayList<>();
        InfoPasajero infoPasajero = new InfoPasajero();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());

            MongoCollection<Document> collectionPasajeros = mongoDatabase.getCollection(COLLECTION_PASAJEROS);

            Bson filter = Filters.eq("pasaporte", pasaporte);
            Document doc = collectionPasajeros.find(filter).first();

            if (doc != null) {
                infoPasajero.setPasajero(gson.fromJson(doc.toJson(), PasajeroEntity.class));
            }

            MongoCollection<Document> collectionReservas = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.match(Filters.eq("pasajero_id", infoPasajero.getPasajero().getId())),
                    Aggregates.lookup("vuelos", "vuelo_id", "_id", "vuelo"),
                    Aggregates.lookup("pasajeros", "pasajero_id", "_id", "pasajero"),
                    Aggregates.unwind("$vuelo"),
                    Aggregates.unwind("$pasajero")
            );

            AggregateIterable<Document> docs = collectionReservas.aggregate(pipeline);
            for (Document document : docs) {
                ReservaWithRelations reserva = gson.fromJson(document.toJson(), ReservaWithRelations.class);
                reservas.add(reserva);
            }

            for (ReservaWithRelations reserva : reservas) {
                VueloWithPrecio vueloWithPrecio = new VueloWithPrecio();
                vueloWithPrecio.setPrecio(reserva.getPrecio());
                vueloWithPrecio.setVuelo(reserva.getVuelo());
                vueloWithPrecio.setEstado(reserva.getEstado());
                vuelos.add(vueloWithPrecio);
            }
            infoPasajero.setVuelos(vuelos);
        }

        return infoPasajero;
    }

    /**
     * Busca reservas por el precio
     *
     * @param cantidad Precio a buscar
     * @return Se devuelve una lista con todas las reserva de ese precio
     */
    @Override
    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        List<ReservaWithRelations> reservas = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatabaseSettings.getDB());
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_RESERVAS);
            List<Bson> pipeline = Arrays.asList(
                    Aggregates.match(Filters.gte("precio", cantidad.doubleValue())),
                    Aggregates.lookup("vuelos", "vuelo_id", "_id", "vuelo"),
                    Aggregates.lookup("pasajeros", "pasajero_id", "_id", "pasajero"),
                    Aggregates.unwind("$vuelo"),
                    Aggregates.unwind("$pasajero")
            );

            AggregateIterable<Document> docs = mongoCollection.aggregate(pipeline);
            for (Document doc : docs) {
                ReservaWithRelations reserva = gson.fromJson(doc.toJson(), ReservaWithRelations.class);
                reservas.add(reserva);
            }
        }
        return reservas;
    }
}
