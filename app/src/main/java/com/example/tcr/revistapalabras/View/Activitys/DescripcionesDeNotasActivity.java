package com.example.tcr.revistapalabras.View.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tcr.revistapalabras.Controler.ControlerContenidoFavoritoFirebase;
import com.example.tcr.revistapalabras.Controler.ControllerPublicidadesFirebase;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.Model.Publicidad;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;


public class DescripcionesDeNotasActivity extends AppCompatActivity {


    public static final String CLAVE_OBJETO_CONTENIDO = "noticia objeto";

    private final String mimeType = "text/html";
    private final String encoding = "UTF-8";

    private Noticia noticia;

    private TextView textViewTituloDeLaNota;
    private ImageView imageViewDeLaNota;
    private ImageView imageViewPublicidad1;
    private ImageView imageViewPublicidad2;
    private TextView textViewContenidoDeLaNota1;
    private TextView textViewContenidoDeLaNota2;
    private FloatingActionButton floatingActionButtonCompartir;
    private FloatingActionButton floatingActionAgregarAFavoritos;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Boolean noticiaFavorita;

    private String primeraParte;
    private String segundaParte;

    private List<String> listaDePublicidades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripciones_de_notas);

        textViewTituloDeLaNota = findViewById(R.id.textViewTituloDeLaNota_activitydescripcionesdenotas);
        imageViewDeLaNota = findViewById(R.id.imageViewDeLaNota_activitydescripcionesdenotas);
        textViewContenidoDeLaNota1 = findViewById(R.id.textViewContenidoDeLaNota1_activitydescripcionesdenotas);
        textViewContenidoDeLaNota2 = findViewById(R.id.textViewContenidoDeLaNota2_activitydescripcionesdenotas);
        floatingActionButtonCompartir = findViewById(R.id.floatingButtonCompartir_activirtdescripcionesdenotas);
        floatingActionAgregarAFavoritos = findViewById(R.id.floatinButtonAgregarAFavoritos_activitydescripcionesdenotas);

        imageViewPublicidad1 = findViewById(R.id.imageViewPublicidad1_activitydescripcionesdelanota);
        imageViewPublicidad2 = findViewById(R.id.imageViewPublicidad2_activitydescripcionesdenotas);


        textViewTituloDeLaNota.requestFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notas Favoritas");

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        noticia = (Noticia) bundle.getSerializable(CLAVE_OBJETO_CONTENIDO);


        try {
            primeraParte = noticia.getContent().getRendered().split("</p>")[0] + noticia.getContent().getRendered().split("</p>")[1];

        }catch (Exception e){
            primeraParte = noticia.getTheDescription().split("</p>")[0] + noticia.getTheDescription().split("</p>")[1];

        }

        primeraParte += "</p>";


        try {
            segundaParte = noticia.getContent().getRendered().replaceFirst(noticia.getContent().getRendered().split("</p>")[0] + "</p>", "");
            segundaParte = segundaParte.replaceFirst(noticia.getContent().getRendered().split("</p>")[1] + "</p>", "");
        }catch (Exception e){
            segundaParte = noticia.getTheDescription().replaceFirst(noticia.getTheDescription().split("</p>")[0] + "</p>", "");
            segundaParte = segundaParte.replaceFirst(noticia.getTheDescription().split("</p>")[1] + "</p>", "");
        }




        try {
            Helper.cargarImagenes(imageViewDeLaNota, getApplicationContext(), noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getFull().getSource_url());

        } catch (Exception e) {
            try {
                Helper.cargarImagenes(imageViewDeLaNota, getApplicationContext(), noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getLarge().getSource_url());

            } catch (Exception e1) {
                try {
                    Helper.cargarImagenes(imageViewDeLaNota, getApplicationContext(), noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium_large().getSource_url());

                }catch (Exception e2){
                    Helper.cargarImagenes(imageViewDeLaNota, getApplicationContext(), noticia.getImagen());

                }

            }
        }


        textViewContenidoDeLaNota1.setText(Html.fromHtml(primeraParte));
        textViewContenidoDeLaNota2.setText(Html.fromHtml(segundaParte));


        try{
            textViewTituloDeLaNota.setText(Html.fromHtml(noticia.getTitle().getRendered()));

        }catch (Exception e){
            textViewTituloDeLaNota.setText(Html.fromHtml(noticia.getTitleS()));

        }


        floatingActionButtonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, "Compartir");
                share.putExtra(Intent.EXTRA_TEXT, "Mira esta nota me gusta mucho  " + noticia.getLink());
                startActivity(Intent.createChooser(share, "Compartir en"));
            }
        });


        floatingActionAgregarAFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    new ControlerContenidoFavoritoFirebase().verficiarSiLaNoticiaEstaEnFirebase(noticia, new ResultListener<Boolean>() {
                        @Override
                        public void finish(Boolean resultado) {

                            if (resultado) {
                                Noticia noticiaFav = new Noticia(noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getFull().getSource_url(),noticia.getId(),noticia.getLink(),noticia.getContent().getRendered(),noticia.getTitle().getRendered(),noticia.getExcerpt().getRendered());
                               new ControlerContenidoFavoritoFirebase().agregarLaNoticiaAGuardado(noticiaFav, new ResultListener<Boolean>() {
                                   @Override
                                   public void finish(Boolean resultado) {
                                       if (resultado){
                                           FancyToast.makeText(getBaseContext(), "Nota agregada a favoritos", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                       }
                                   }
                               });

                            } else {

                                FancyToast.makeText(getBaseContext(), "Ya ha sido  agregada a favoritos", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        }
                    });

                } else

                {
                    startActivity(new Intent(DescripcionesDeNotasActivity.this, LoginActivity.class));
                }
            }
        });


        new ControllerPublicidadesFirebase().traerListaDePublicidades(new ResultListener<List<Publicidad>>() {
            @Override
            public void finish(List<Publicidad> resultado) {


                imageViewPublicidad1.setOnClickListener(new ClickPublicidad(resultado.get(0).getLink()));
                imageViewPublicidad2.setOnClickListener(new ClickPublicidad(resultado.get(1).getLink()));

                cargarPublicidad(resultado.get(0).getUrl(), imageViewPublicidad1);
                cargarPublicidad(resultado.get(1).getUrl(), imageViewPublicidad2);


            }
        });
    }


    public void cargarPublicidad(String url, ImageView imageView) {

        if (url.equals("")) {
            return;
        }

        if (url.endsWith(".gif")) {
            Glide.with(getApplicationContext())
                    .load(url)
                    .asGif()
                    .crossFade()
                    .into(imageView);
        } else {
            Glide.with(getApplicationContext())
                    .load(url)
                    .into(imageView);
        }
    }

    public class ClickPublicidad implements View.OnClickListener{
        private String link;

        ClickPublicidad(String link){
            this.link = link;
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            startActivity(i);
        }
    }
}
