package com.giovanny.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Object o = getListAdapter().getItem(position);
        Toast.makeText(this, "Selección: " + Integer.toString(position) + " - "
                + o.toString(), Toast.LENGTH_LONG).show();
    }
}
