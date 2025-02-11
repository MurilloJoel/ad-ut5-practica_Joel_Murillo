package org.educa.service;

import org.educa.dao.VueloDAO;
import org.educa.dao.VueloDAOImpl;
import org.educa.wrappers.BeneficioVuelo;

import java.util.List;

public class VueloService {
    VueloDAO vueloDAO= new VueloDAOImpl();
    public List<BeneficioVuelo> getBeneficioVuelo(){
        return vueloDAO.getBeneficioVuelo();
    }
}
