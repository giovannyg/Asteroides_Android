package com.giovanny.asteroides;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.R.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class Inicio extends AppCompatActivity implements View.OnClickListener {

    private Button btn_jugar, btn_configurar, btn_about, btn_salir, btn_puntuaciones;
    private Menu menu;
    AlertDialog.Builder builder;
    public static AlmacenPuntuaciones almacen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

/*        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        iniciarVistas();
    }


    public void iniciarVistas(){
        almacen = new AlmacenPuntuacionesArray();

        btn_jugar = (Button) findViewById(R.id.jugar);
        btn_configurar = (Button) findViewById(R.id.configurar);
        btn_about = (Button) findViewById(R.id.about);
        btn_puntuaciones = (Button) findViewById(R.id.puntuaciones);
        btn_salir = (Button) findViewById(R.id.salir);

        btn_jugar.setOnClickListener(this);
        btn_configurar.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_puntuaciones.setOnClickListener(this);
        btn_salir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getTag() != null)

        switch((String)v.getTag()) {
            case "jugar":
                mostrarPreferencias();
                break;
            case "configurar":
                startActivity(new Intent(this, Preferencias.class));
                break;
            case "about":
                startActivity(new Intent(this, About.class));
                break;
            case "puntuaciones":
                /*startActivity(new Intent(this, Puntuaciones.class));*/
                break;
            case  "salir":
                alertSalida();
                break;
        }
    }

    public void alertSalida() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Esta seguro que desea salir?")
                .setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    /*dialog.dismiss();*/
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK) {
            alertSalida();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true; //El menú ya está visible
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
         switch (item.getItemId()) {
             case R.id.config:
                 startActivity(new Intent(this, Preferencias.class));
                 break;
             case R.id.about:
                 startActivity(new Intent(this, About.class));
                 break;
         }
        return true; //Consumimos el item para que no se propague
    }
    public void mostrarPreferencias() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String s = "musica" + preferences.getBoolean("musica", true)
                            + preferences.getString("graficos", "?");

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
