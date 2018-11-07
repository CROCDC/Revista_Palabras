package com.example.tcr.revistapalabras.View.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.tcr.revistapalabras.R;

public class PantallaDeCargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PantallaDeCargaActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.entrada_por_arriba_activity,R.anim.salida_por_abajo);

                finish();
            }
        },2000);
    }
}
