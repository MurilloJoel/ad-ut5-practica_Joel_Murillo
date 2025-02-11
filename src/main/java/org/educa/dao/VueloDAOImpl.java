package org.educa.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.educa.entity.AeropuertoEntity;
import org.educa.entity.ReservaEntity;
import org.educa.entity.VueloWithRelations;
import org.educa.settings.DatabaseSettings;
import org.educa.wrappers.BeneficioVuelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VueloDAOImpl implements VueloDAO {
    private final MongoCollection<Document> vuelos;
    private final MongoCollection<Document> reservas;

    public VueloDAOImpl() {
        // Inicializar el cliente de MongoDB
        MongoClient mongoClient = MongoClients.create(DatabaseSettings.getURL());
        MongoDatabase database = mongoClient.getDatabase(DatabaseSettings.getDB());

        // Inicializar las colecciones
        this.vuelos = database.getCollection("vuelos");
        this.reservas = database.getCollection("reservas");
    }
    @Override
    public List<BeneficioVuelo> getBeneficioVuelo() {

        for (VueloWithRelations vuelo : vuelos) {
            List<ReservaEntity> reservas = findReservasByVueloId(vuelo.getId());
            BigDecimal totalRecaudado = BigDecimal.ZERO;
            int totalPasajeros = 0;

            for (ReservaEntity reserva : reservas) {
                BigDecimal monto = reserva.getEstado().equalsIgnoreCase("cancelada")
                        ? reserva.getPrecio().divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP)
                        : reserva.getPrecio();
                totalRecaudado = totalRecaudado.add(monto);
                totalPasajeros += 1; // Cada reserva representa un pasajero
            }

            BeneficioVuelo beneficio = new BeneficioVuelo(
                    vuelo.getCodigoVuelo(),
                    vuelo.getOrigen().getNombre(),
                    vuelo.getDestino().getNombre(),
                    totalPasajeros,
                    totalRecaudado,
                    vuelo.getCoste(),
                    totalRecaudado.subtract(vuelo.getCoste())
            );

            beneficios.add(beneficio);
        }

        return beneficios;
}
    private List<VueloWithRelations> findAllVuelos() {
        List<VueloWithRelations> vuelos = new ArrayList<>();

        for (Document doc : vuelos.find()) {
            VueloWithRelations vuelo = new VueloWithRelations(
                    doc.getInteger("_id"),
                    doc.getString("codigo_vuelo"),
                    parseAeropuerto(doc.get("origen", Document.class)),
                    parseAeropuerto(doc.get("destino", Document.class)),
                    doc.getInteger("duracion"),
                    doc.getString("estado"),
                    doc.getString("fecha"),
                    doc.get("coste", BigDecimal.class)
            );
            vuelos.add(vuelo);
        }

        return vuelos;
    }

    private List<ReservaEntity> findReservasByVueloId(Integer vueloId) {
        List<ReservaEntity> reservas = new ArrayList<>();

        for (Document doc : reservas.find(eq("vuelo_id", vueloId))) {
            ReservaEntity reserva = new ReservaEntity(
                    doc.getInteger("_id"),
                    doc.getInteger("vuelo_id"),
                    doc.getInteger("pasajero_id"),
                    doc.getString("asiento"),
                    doc.getString("estado"),
                    doc.get("precio", BigDecimal.class)
            );
            reservas.add(reserva);
        }

        return reservas;
    }

    private AeropuertoEntity parseAeropuerto(Document aeropuertoDoc) {
        if (aeropuertoDoc == null) {
            return null;
        }
        return new AeropuertoEntity(
                aeropuertoDoc.getInteger("_id"),
                aeropuertoDoc.getString("nombre"),
                aeropuertoDoc.getString("ciudad"),
                aeropuertoDoc.getString("pais"),
                aeropuertoDoc.getString("codigo_IATA")
        );
    }
}
    }

