package org.educa.service;

import org.educa.dao.VueloDAO;
import org.educa.dao.VueloDAOImpl;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.BeneficioVuelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Servicio que contiene los metodos que llaman a los propios de la clase VueloDAOImpl y el calculo .
 *
 * @author Joel Murillo Masa
 */


public class VueloService {
    VueloDAO vueloDAO = new VueloDAOImpl();

    /**
     * Obtiene los beneficios de los vuelos
     *
     * @return Se devuelve una lista con todos los beneficios.
     */
    public List<BeneficioVuelo> getBeneficioVuelo() {
        List<BeneficioVuelo> vuelos = vueloDAO.getBeneficioVuelo();
        //Lista con ReservaWithRelations
        List<ReservaWithRelations> reservas = vueloDAO.findAll();
        //Bucle para calcular el beneficio y el total de cada vuelo
        for (BeneficioVuelo beneficioVuelo : vuelos) {
            BigDecimal recaudado = BigDecimal.ZERO;

            for (ReservaWithRelations reserva : reservas) {

                recaudado = recaudado.add(calcular(reserva, beneficioVuelo));
            }

            beneficioVuelo.setTotal(recaudado);
            beneficioVuelo.setBeneficio(recaudado.subtract(beneficioVuelo.getCoste()));
        }

        return vuelos;
    }

    /**
     * El metodo calcular devuelve el total considerando que, si esta cancelada,el precio se reduce a la mitad y que deben coincidir el codigo del vuelo de la reserva con el del beneficioVuelo.
     *
     * @param reserva        Reserva a calcular
     * @param beneficioVuelo Beneficio a calcular
     * @return Devuelve el valor del beneficio
     */
    private BigDecimal calcular(ReservaWithRelations reserva, BeneficioVuelo beneficioVuelo) {
        BigDecimal total = BigDecimal.ZERO;
        if (reserva.getVuelo().getCodigoVuelo().equalsIgnoreCase(beneficioVuelo.getCodigoVuelo())) {
            if (reserva.getEstado().equalsIgnoreCase("cancelada")) {
                total = total.add(reserva.getPrecio().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
            } else {
                total = total.add(reserva.getPrecio());
            }
        }
        return total;
    }


}