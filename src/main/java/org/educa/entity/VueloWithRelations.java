package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad correspondiente a la coleccion Vuelos, pero con las relaciones pertinentes con Aeropuerto
 *
 * @author Joel Murillo Masa
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueloWithRelations implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("codigo_vuelo")
    private String codigoVuelo;
    @SerializedName("origen")
    private AeropuertoEntity origen;
    @SerializedName("destino")
    private AeropuertoEntity destino;
    private Integer duracion;
    private String estado;
    private String fecha;
    private BigDecimal coste;
}
