package com.giovanny.asteroides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by giovannyg on 02/08/2016.
 */
public class Boot extends Activity {

    Context contexto;
    Timer timer;
    TimerTask task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        contexto = getApplicationContext();

        task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(contexto, Inicio.class));
            }
        };
        timer = new Timer();
        timer.schedule(task, 2000);
    }
}
