package com.giovanny.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.R.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Inicio extends Activity implements View.OnClickListener {

    private Button btn_jugar, btn_configurar, btn_about, btn_salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            iniciarVistas();
        }
    }

 /*   public boolean onKeyDown(int keyCode, KeyEvent e){
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, e);
    }*/

    public void iniciarVistas(){
        btn_about = (Button) findViewById(R.id.about);

        btn_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getTag() != null)

        switch((String)v.getTag()) {
            case "about":
                startActivity(new Intent(this, About.class));
                break;
            case  "salir":
                finish();
                break;
        }
    }
}
