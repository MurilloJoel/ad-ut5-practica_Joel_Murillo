package org.educa.service;

import org.educa.dao.ReservaDAO;
import org.educa.dao.ReservaDAOImpl;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio que contiene los metodos que llaman a los propios de la clase ReservaDAOImpl
 *
 * @author Joel Murillo Masa
 */


public class ReservaService {
    ReservaDAO reservaDAO = new ReservaDAOImpl();

    /**
     * Este metodo busca por el id, la reserva correspondiente
     *
     * @param id id de la Reserva a buscar
     * @return Se devuelve una Reserva
     */
    public ReservaEntity findById(int id) {
        return reservaDAO.findById(id);
    }

    /**
     * Busca todas las reservas
     *
     * @return Se devuelve una lista con todas las reservas
     */
    public List<ReservaEntity> findAll() {
        return reservaDAO.findAll();
    }

    /**
     * Este metodo busca un pasajero a traves de su pasaporte
     *
     * @param pasaporte Pasaporte a buscar
     * @return Se devuelve la informacion del pasajero
     */
    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        return reservaDAO.findReservasByPasaporte(pasaporte);
    }

    /**
     * Este metodo busca las reservas que tengan un precio mayor o igual que el especificado.
     *
     * @param cantidad Precio a comprobar
     * @return Se devuelve una lista de reservas
     */
    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        return reservaDAO.findReservasByCantidad(cantidad);
    }


    /**
     * Este metodo guarda una reserva y te devuelve el id de dicha reserva
     *
     * @param reserva Reserva a añadir
     * @return Se devuelve el id de la reserva introducida
     */
    public Integer save(ReservaEntity reserva) {
        return reservaDAO.save(reserva);
    }

    /**
     * Modifica la reserva introducida como parametro, y te devuelve la id de la reserva actualizada
     *
     * @param reservaToUpdate Reserva a actualizar
     * @return Se devuelve el numero de reservas actualizadas
     */
    public Long update(ReservaEntity reservaToUpdate) {
        return reservaDAO.update(reservaToUpdate);
    }

    /**
     * Borra la Reserva que corresponda con el id especificado
     *
     * @param id ID de la reserva a eliminar
     * @return Se devuelve el numero de reservas borradas
     */
    public Long delete(int id) {
        return reservaDAO.delete(id);
    }

}
