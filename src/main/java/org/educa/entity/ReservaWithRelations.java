package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad correspondiente a la coleccion Reservas, pero con las relaciones con Vuelos y Pasajeros
 *
 * @author Joel Murillo Masa
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaWithRelations implements Serializable {

    @SerializedName("_id")
    private Integer id;
    @SerializedName("vuelo")
    private VueloEntity vuelo;
    @SerializedName("pasajero")
    private PasajeroEntity pasajero;
    private String asiento;
    private String estado;
    private BigDecimal precio;
}
