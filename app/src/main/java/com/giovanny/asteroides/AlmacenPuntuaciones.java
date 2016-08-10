package com.giovanny.asteroides;

import java.util.Vector;

/**
 * Created by giovannyg on 09/08/2016.
 */
public interface AlmacenPuntuaciones {
    public void guardarPuntuaciones(int puntos, String nombre, long fecha);
    public Vector<String> listaPuntuaciones(int cantidad);
}
