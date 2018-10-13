package com.example.tcr.revistapalabras.View.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Adapter.NoticiasAdapter;

import java.util.List;

public class BusquedaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListaDeNoticiasDeLaBusqueda;
    private NoticiasAdapter noticiasAdapter;

    public static final String CLAVE_VALOR_DE_BUSQUEDA = "busqueda";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificarTouchCelda(Noticia unNoticia) {

            }

            @Override
            public void notificarTouchPublicidad(String link) {

            }

            @Override
            public void notificarTouchRedSocial(Integer numero) {

            }
        });


        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String busquedaRealizada = (String) bundle.get(CLAVE_VALOR_DE_BUSQUEDA);

        new ControlerNoticias().pedirListaDeNoticiasDeLaBusqueda(busquedaRealizada, new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                noticiasAdapter.setListaDeNoticias(resultado);

            }
        });

        recyclerViewListaDeNoticiasDeLaBusqueda = findViewById(R.id.recycleViewBusqueda_activitybusqueda);


        recyclerViewListaDeNoticiasDeLaBusqueda.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerViewListaDeNoticiasDeLaBusqueda.setLayoutManager(linearLayoutManager);

        recyclerViewListaDeNoticiasDeLaBusqueda.setAdapter(noticiasAdapter);


    }
}
