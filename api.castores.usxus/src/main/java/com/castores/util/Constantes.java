package com.castores.util;

/**
 *
 * @author Luis.Bonifaz
 */
public class Constantes {

    private Constantes() {
        throw new IllegalStateException("No existe un constructor para la clase Constantes");
    }

    public static final String MENSAJE = "mensaje";
    public static final String CANTIDAD_REGISTRO = "ID no se permite incorporar una cantidad a un registro que no existe en el catalogo.";
    public static final String CANTIDAD_INCTIVO = "No se puede incorporar una cantidad en un producto inactivo.";
    public static final String RESTA_INCTIVO = "No se puede restar una cantidad en un producto inactivo.";
    public static final String CANTIDAD_MAX = "La cantidad debe ser mayor que 0.";
    public static final String RESTA_REGISTRO = "ID no se permite restar una cantidad a un registro que no existe en el catalogo.";
    public static final String RESTA_NEGATIVO = "Error: No se puede restar la cantidad, el inventario no puede quedar en negativo.";

}
