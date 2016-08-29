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
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by giovannyg on 18/08/2016.
 */
public class VistaJuego extends View {

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

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);

        Drawable drawableNave, drawableAsteroide, drawableMisil;

        SharedPreferences pref = context.getSharedPreferences("com.giovanny.asteroides_preferences", Context.MODE_PRIVATE);
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

            setBackgroundColor(Color.BLACK);
        } else {
            drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
        }

        nave = new Grafico(this, drawableNave);

        Asteroides = new Vector<Grafico>();

        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo(Math.random() * 360);
            asteroide.setRotacion(Math.random() * 8 - 4);
            Asteroides.add(asteroide);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide : Asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
        nave.dibujaGrafico(canvas);
    }
}
