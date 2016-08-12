package com.giovanny.asteroides;

import android.app.ListActivity;
import android.os.Bundle;

import com.giovanny.asteroides.Adapters.AdaptadorPuntuacion;


/**
 * Created by giovannyg on 10/08/2016.
 */
public class Puntuacion extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        setListAdapter(new AdaptadorPuntuacion(this,
                Inicio.almacen.listaPuntuaciones(10)));
    }
}
