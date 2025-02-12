package org.educa.dao;

import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz que declara los metodos relacionados con la reserva.
 *
 * @author Joel Murillo Masa
 */
public interface ReservaDAO {
    Integer save(ReservaEntity reserva);

    List<ReservaEntity> findReservasByVueloId(Integer vueloId);

    InfoPasajero findReservasByPasaporte(String pasaporte);

    List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad);

    ReservaEntity findById(int id);

    Long update(ReservaEntity reservaToUpdate);

    Long delete(int id);

    List<ReservaEntity> findAll();

}
