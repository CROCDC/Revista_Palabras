package com.example.tcr.revistapalabras.View.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcr.revistapalabras.Controler.ControlerContenidoFavoritoFirebase;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;


public class DescripcionesDeNotasActivity extends AppCompatActivity {



    public static final String CLAVE_OBJETO_CONTENIDO = "noticia objeto";

    private final String mimeType = "text/html";
    private final String encoding = "UTF-8";

    private Noticia noticia;

    private TextView textViewTituloDeLaNota;
    private ImageView imageViewDeLaNota;
    private TextView textViewContenidoDeLaNota;
    private WebView webViewContenidoDeLaNotas;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonCompartir;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionAgregarAFavoritos;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static Boolean noticiaFavorita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripciones_de_notas);

        textViewTituloDeLaNota = findViewById(R.id.textViewTituloDeLaNota_activitydescripcionesdenotas);
        imageViewDeLaNota = findViewById(R.id.imageViewDeLaNota_activitydescripcionesdenotas);
        webViewContenidoDeLaNotas = findViewById(R.id.webViewContenidoDeLaNota_activitydescripcionesdenotas);
        floatingActionButtonCompartir = findViewById(R.id.floatingButtonCompartir_activirtdescripcionesdenotas);
        floatingActionAgregarAFavoritos = findViewById(R.id.floatinButtonAgregarAFavoritos_activitydescripcionesdenotas);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Notas Favoritas");

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        noticia = (Noticia) bundle.getSerializable(CLAVE_OBJETO_CONTENIDO);


        Helper.cargarImagenes(imageViewDeLaNota, getBaseContext(), noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium().getSource_url());


        webViewContenidoDeLaNotas.loadData(noticia.getContent().getRendered(), mimeType, encoding);


        textViewTituloDeLaNota.setText(noticia.getTitle().getRendered());


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
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference(Helper.REFERENCIA_CONTENIDO_FAVORITO).child(user.getUid());
                                databaseReference.child(noticia.getId().toString()).setValue(new Noticia(noticia.getId(), noticia.getLink(), noticia.getTitle(), noticia.getContent(), noticia.getExcerpt(), noticia.getEmbedded(), noticia.getCategories()));
                                FancyToast.makeText(getBaseContext(), "Nota agregada a favoritos", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
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
}

}
