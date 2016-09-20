package com.giovanny.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.EventListener;
import java.util.List;
import java.util.Vector;

/**
 * Created by giovannyg on 18/08/2016.
 */
public class VistaJuego extends View implements SensorEventListener {

    /*ASTEROIDES*/
    private Vector<Grafico> Asteroides; //vector con los asteroides
    private int numAsteroides = 5; //Numero inicial de asteroides
    private int numFragmentos = 3; //Fragmentos en que se divide

    /*NAVE*/
    private Grafico nave; //Grafico de la nave
    private int giroNave; //Incremento de direccion
    private float aceleracionNave; //aumento de velocidad
    /*incremento standard de giro y aceleración*/
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    /*THREAD Y TIEMPO*/
    //Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    //Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    //Cuando se realizó el ultimo proceso
    private long ultimoProceso = 0;

    private float mX = 0, mY = 0;
    private boolean disparo = false;

    /*MISIL*/
    /*private Grafico misil;*/
    private Vector<Grafico> Misiles;
    public static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo = false;
    private int tiempoMisil;
    Drawable drawableMisil;

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide;

        SharedPreferences pref = context
                .getSharedPreferences("com.giovanny.asteroides_preferences", Context.MODE_PRIVATE);

        /*Selección de graficos vectoriales*/
        if(pref.getString("graficos", "1").equals("0")) {
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);

            ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicWidth(50);
            dAsteroide.setIntrinsicHeight(50);
            drawableAsteroide = dAsteroide;

            Path pathNave = new Path();
            pathNave.moveTo((float) 0.9, (float) 0.0);
            pathNave.lineTo((float) 0.0, (float) 0.0);
            pathNave.lineTo((float) 1.0, (float) 0.5);
            pathNave.lineTo((float) 0.0, (float) 1.0);
            pathNave.lineTo((float) 1.0, (float) 0.5);

            ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave, 1, 1));
            dNave.getPaint().setColor(Color.RED);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicWidth(60);
            dNave.setIntrinsicHeight(45);
            drawableNave = dNave;

            //Misil para graficos vectoriales
            ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);
            drawableMisil = dMisil;

            setBackgroundColor(Color.BLACK);
        } else {
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
            drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
        }

        nave = new Grafico(this, drawableNave);
        Asteroides = new Vector<Grafico>();
        Misiles = new Vector<Grafico>();

        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo(Math.random() * 360);
            asteroide.setRotacion(Math.random() * 8 - 4);
            Asteroides.add(asteroide);
        }

        String sensorActivo = pref.getString("sensores", "2");
        SensorManager msensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        switch (sensorActivo) {
            case "0":
                List<Sensor> listaSensores = msensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

                if (!listaSensores.isEmpty()) {
                    Sensor orientationSensor = listaSensores.get(0);
                    msensorManager.registerListener(this, orientationSensor, msensorManager.SENSOR_DELAY_GAME);
                }
                break;
            case "1":
                List<Sensor> listaSensores2 = msensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

                if (!listaSensores2.isEmpty()) {
                    Sensor acelerometerSensor = listaSensores2.get(0);
                    msensorManager.registerListener(this, acelerometerSensor, msensorManager.SENSOR_DELAY_GAME);
                }
                break;
        }
    }

    synchronized
    protected void actualizaFisica() {

        long ahora = System.currentTimeMillis();
        //No hagas nada si el periodo de proceso no se ha cumplido
        if(ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }
        //Para una ejecución en tiempo real, calculemos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; //Para la proxima vez
         /*Actualizamos la velocidad y dirección de la nave a partir de giroNave y aceleracionNave
          segun la entrada del jugador*/
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));

        double nIncX = nave.getIncX() + aceleracionNave *
                Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getIncY() + aceleracionNave *
                Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        //Actualizamos si el modulo de la velocidad no excede el maximo
        if(Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()) {
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        //Actualizamos las posiciones X e Y
        nave.incrementaPos(retardo);
        for(Grafico asteroide : Asteroides) {
            asteroide.incrementaPos(retardo);
        }


        //Actualizamos posición del misil
        if(misilActivo) {
            for(Grafico misil : Misiles) {
                misil.incrementaPos(retardo);
                tiempoMisil -=retardo;
                if(tiempoMisil < 0) {
                    misilActivo = false;
                    Misiles.clear();
                }
                for (int i = 0; i < Asteroides.size(); i++) {
                    if (misil.verificaColision(Asteroides.elementAt(i))) {
                        destruyeAsteroide(i);
                        break;
                    }
                }
            }
        }
    }

    private void destruyeAsteroide(int i) {
        Asteroides.remove(i);
        misilActivo = false;
    }
    private void activaMisil() {

            Grafico misil = new Grafico(this, drawableMisil);
            misil.setPosX(nave.getPosX() + nave.getAncho() / 2 - misil.getAncho() / 2);
            misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAncho() / 2);
            misil.setAngulo(nave.getAngulo());
            misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
            misil.setIncY(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
            Misiles.add(misil);


            tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.
                    getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;
    }

    private boolean hayValorInicial = false;
    private float valorInicial;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[1];

        if(!hayValorInicial) {
            valorInicial = valor;
            hayValorInicial = true;
        }
        giroNave = (int) (valor-valorInicial) / 3;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public ThreadJuego getThread() {
        return thread;
    }

    class ThreadJuego extends Thread {

        private boolean pausa, corriendo;

        public synchronized void pausar(){
            pausa = true;
        }
        public synchronized  void reanudar() {
            pausa = false;
            notify();
        }
        public void detener() {
            corriendo = false;
            if(pausa) reanudar();
        }

        @Override
        public void run() {
            corriendo = true;

            while(corriendo) {
                actualizaFisica();
                synchronized(this) {
                    while(pausa) {
                        try {
                            wait();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
    }
    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        //Una vez que conocemos nuestro ancho y alto
        for(Grafico asteroide : Asteroides) {
            do {
                asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
                asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));

            } while(asteroide.distancia(nave) < (ancho+alto)/5);
        }

        nave.setPosX((double)ancho/2);
        nave.setPosY((double)alto/2);

        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    synchronized
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for (Grafico asteroide : Asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
        nave.dibujaGrafico(canvas);
        if(misilActivo){
            for(Grafico misil : Misiles) {
                misil.dibujaGrafico(canvas);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs( y - mY);

                if (dy < 5 && dx > 3) {
                    giroNave = Math.round((mY - y) / 2);
                    disparo = false;
                }
                else if (dx < 5 && dy > 3) {
                    aceleracionNave = Math.round((mY - y) /20);
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if(disparo) {
                    activaMisil();
                }
                break;
        }
        mX = x; mY = y;
        return true;
    }
}
