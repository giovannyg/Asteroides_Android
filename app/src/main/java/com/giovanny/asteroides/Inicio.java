package com.giovanny.asteroides;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Inicio extends AppCompatActivity implements View.OnClickListener, GestureOverlayView.OnGesturePerformedListener {

    private Button btn_jugar, btn_configurar, btn_about, btn_salir, btn_puntuaciones;
    private Menu menu;
    private TextView titulo;
    AlertDialog.Builder builder;
    public static AlmacenPuntuaciones almacen;
    private GestureLibrary libreria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

/*        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/
        /*Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();*/
        iniciarVistas();
    }

/*    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }*/

    public void iniciarVistas(){
        almacen = new AlmacenPuntuacionesArray();

        btn_jugar = (Button) findViewById(R.id.jugar);
        btn_configurar = (Button) findViewById(R.id.configurar);
        btn_about = (Button) findViewById(R.id.about);
        btn_puntuaciones = (Button) findViewById(R.id.puntuaciones);
        btn_salir = (Button) findViewById(R.id.salir);
        titulo = (TextView) findViewById(R.id.titulo);

        /*Eventos*/
        btn_jugar.setOnClickListener(this);
        btn_configurar.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_puntuaciones.setOnClickListener(this);
        btn_salir.setOnClickListener(this);

        /*Animaciones*/
        startAnimations();

        libreria = GestureLibraries.fromRawResource(this, R.raw.gesture);

        if(!libreria.load()) {
            finish();
        }
        GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureView.addOnGesturePerformedListener(this);
    }

    public void startAnimations() {

        /*View animations*/
        Animation animacion_titulo = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        titulo.startAnimation(animacion_titulo);

        Animation animacion_btn_configurar = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        btn_configurar.startAnimation(animacion_btn_configurar);

        /*Property Animations*/
        Animator animacion_btn_play = AnimatorInflater.loadAnimator(this, R.animator.transparencia);
        animacion_btn_play.setTarget(btn_jugar);

        ObjectAnimator animacion_btn_puntuaciones = ObjectAnimator.ofFloat(btn_puntuaciones, "alpha", 0f, 1f);
        animacion_btn_puntuaciones.setDuration(5000);

        Animator animacion_btn_salir = AnimatorInflater.loadAnimator(this, R.animator.transparencia);
        animacion_btn_salir.setTarget(btn_salir);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animacion_btn_play, animacion_btn_puntuaciones, animacion_btn_salir);
        animatorSet.start();
    }

    public void setAlpha() {}

    @Override
    public void onClick(View v) {

        if(v.getTag() != null)

            switch((String)v.getTag()) {
                case "jugar":
                /*mostrarPreferencias();*/
                    startActivity(new Intent(this, Juego.class));
                    break;
                case "configurar":
                    startActivity(new Intent(this, Preferencias.class));
                    break;
                case "about":
                    Animation animacion_btn_acercade = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
                    btn_about.startAnimation(animacion_btn_acercade);
                    startActivity(new Intent(this, About.class));
                    break;
                case "puntuaciones":
                    startActivity(new Intent(this, Puntuacion.class));
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

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        ArrayList<Prediction> predictions = libreria.recognize(gesture);
        if(predictions.size() > 0) {
            String comando = predictions.get(0).name;
            switch (comando) {
                case "play":
                    startActivity(new Intent(this, Juego.class));
                    break;
                case "configurar":
                    startActivity(new Intent(this, Preferencias.class));
                    break;
                case "acerca_de":
                    startActivity(new Intent(this, About.class));
                    break;
                case "cancelar":
                    finish();
                    break;
            }
        }
    }
}
