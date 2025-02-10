package org.educa.service;

import org.educa.dao.ReservaDAO;
import org.educa.dao.ReservaDAOImpl;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.List;

public class ReservaService {
    ReservaDAO reservaDAO= new ReservaDAOImpl();
    public List<ReservaEntity> findReservasByVueloId(Integer vueloId) {
        return reservaDAO.findReservasByVueloId(vueloId);
    }

    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        return reservaDAO.findReservasByPasaporte(pasaporte);
    }

    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        return reservaDAO.findReservasByCantidad(cantidad);
    }

    public Integer save(ReservaEntity reserva) {
        return reservaDAO.save(reserva);
    }

    public ReservaEntity findById(int id) {
        return reservaDAO.findById(id);
    }

    public Long update(ReservaEntity reservaToUpdate) {
        return reservaDAO.update(reservaToUpdate);
    }

    public Long delete(int id) {
        return reservaDAO.delete(id);
    }

    public List<ReservaEntity> findAll() {
        return reservaDAO.findAll();
    }
}
