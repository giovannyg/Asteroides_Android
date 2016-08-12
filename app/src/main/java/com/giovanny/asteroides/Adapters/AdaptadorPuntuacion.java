package com.giovanny.asteroides.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.giovanny.asteroides.R;

import java.util.Vector;

/**
 * Created by giovannyg on 11/08/2016.
 */
public class AdaptadorPuntuacion extends BaseAdapter {

    private final Activity actividad;
    private final Vector<String> lista;

    public AdaptadorPuntuacion(Activity actividad, Vector<String> lista) {
        super();
        this.actividad = actividad;
        this.lista = lista;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = actividad.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_puntuacion, null, true);
        TextView textView = (TextView)view.findViewById(R.id.titulo);
        textView.setText(lista.elementAt(position));
        ImageView imageView = (ImageView)view.findViewById(R.id.icono);

        switch (Math.round((float)Math.random() * 3)) {
            case 0:
                imageView.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.asteroide2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.asteroide3);
                break;
        }
        return view;
    }

    @Override
    public int getCount() {

        return lista.size();
    }

    @Override
    public Object getItem(int position) {

        return lista.elementAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
