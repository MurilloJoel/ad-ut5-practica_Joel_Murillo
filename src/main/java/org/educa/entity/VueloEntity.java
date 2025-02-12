package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad correspondiente a la coleccion de Vuelos de la base de datos
 *
 * @author Joel Murillo Masa
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueloEntity implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("codigo_vuelo")
    private String codigoVuelo;
    @SerializedName("origen_id")
    private Integer origenId;
    @SerializedName("destino_id")
    private Integer destinoId;
    private Integer duracion;
    private String estado;
    private String fecha;
    private BigDecimal coste;
}
