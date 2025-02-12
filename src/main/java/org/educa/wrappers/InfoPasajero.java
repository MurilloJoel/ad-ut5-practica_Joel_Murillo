package org.educa.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.educa.entity.PasajeroEntity;

import java.util.List;

/**
 * Wrapper para mostrar los datos correspondiente a mostrar la informacion de los Pasajeros y sus Vuelos con los precios
 *
 * @author Joel Murillo Masa
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoPasajero {
    private PasajeroEntity pasajero;
    private List<VueloWithPrecio> vuelos;
}
