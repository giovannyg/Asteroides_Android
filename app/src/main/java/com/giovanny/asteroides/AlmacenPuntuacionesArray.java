package com.giovanny.asteroides;

import java.util.Vector;

/**
 * Created by giovannyg on 09/08/2016.
 */
public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones {
    private Vector<String> puntuaciones;

    public AlmacenPuntuacionesArray(){
        puntuaciones = new Vector<String>();
        puntuaciones.add("123000 Pepito Dominguez");
        puntuaciones.add("111000 Pedro Martinez");
        puntuaciones.add("011000 Paco Perez");
    }
    @Override
    public void guardarPuntuaciones(int puntos, String nombre, long fecha) {
        puntuaciones.add(0, puntos + " " + nombre);
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {

        return puntuaciones;
    }
}
