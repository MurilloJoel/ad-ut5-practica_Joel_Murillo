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
        return null;
    }

    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        return null;
    }

    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        return null;
    }

    public Integer save(ReservaEntity reserva) {
        return reservaDAO.save(reserva);
    }

    public ReservaEntity findById(int id) {
        return null;
    }

    public Long update(ReservaEntity reservaToUpdate) {
        return null;
    }

    public Long delete(int id) {
        return null;
    }

    public List<ReservaEntity> findAll() {
        return null;
    }
}
