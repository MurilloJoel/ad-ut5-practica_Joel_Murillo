package org.educa.dao;

import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.BeneficioVuelo;

import java.util.List;

/**
 * Interfaz que declara los metodos relacionados con los vuelos.
 *
 * @author Joel Murillo Masa
 */

public interface VueloDAO {
    List<BeneficioVuelo> getBeneficioVuelo();

    List<ReservaWithRelations> findAll();
}
