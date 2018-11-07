package com.example.tcr.revistapalabras.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tcr.revistapalabras.Controler.ControlerContenidoFavoritoFirebase;
import com.example.tcr.revistapalabras.Model.Footer;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Adapter.NoticiasAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContenidoFavorito extends Fragment {

    public static final String CLAVE_NOMBRE_DE_LA_CATEGORIA = "nombre de la categoriaa";

    private RecyclerView recyclerViewFavoritos;
    private TextView textViewNombreDeLaCategoria;
    private static NoticiasAdapter noticiasAdapter;
    private static I_NotificadorHaciaMainActivity notificador;

    private static ProgressBar progressBar;



    public FragmentContenidoFavorito() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        notificador = (I_NotificadorHaciaMainActivity) context;
    }

    public static FragmentContenidoFavorito fabricaDeFragmentContenidoFavorito() {

        Bundle bundle = new Bundle();

        bundle.putString(CLAVE_NOMBRE_DE_LA_CATEGORIA, "Favoritos");

        FragmentContenidoFavorito fragmentContenidoFavorito = new FragmentContenidoFavorito();

        fragmentContenidoFavorito.setArguments(bundle);

        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificarTouchCelda(Noticia unaNoticia) {
                notificador.notificar(unaNoticia);
            }

            @Override
            public void notificarTouchPublicidad(String link) {
                notificador.notificarTouchPublicidad(link);
            }

            @Override
            public void notificarTouchRedSocial(Integer numero) {
                notificador.notificarTouchRedSocial(numero);
            }
        });
        new ControlerContenidoFavoritoFirebase().pedirListaDeNoticiasFavoritas(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                noticiasAdapter.setListaDeNoticias(resultado);
                noticiasAdapter.agregarFooter(new Footer());
                progressBar.setVisibility(View.INVISIBLE);

                if (resultado.isEmpty()){
                    notificador.notificarSinResultados();
                }

            }
        });

        return fragmentContenidoFavorito;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contenido_favorito, container, false);

        Bundle bundle = getArguments();

        String nombreDeLaCategoria = bundle.getString(CLAVE_NOMBRE_DE_LA_CATEGORIA);

        textViewNombreDeLaCategoria = view.findViewById(R.id.textViewNombreDeLaCategoria_fragmentcontenidofavorito);
        recyclerViewFavoritos = view.findViewById(R.id.recyclerViewCategorias_fragmentcontenidofavorito);
        progressBar = view.findViewById(R.id.progressbar_framgnetcontenidofavorito);

        recyclerViewFavoritos.setHasFixedSize(true);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);



        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation);

        recyclerViewFavoritos.setLayoutAnimation(layoutAnimationController);

        recyclerViewFavoritos.scheduleLayoutAnimation();

        recyclerViewFavoritos.setLayoutManager(linearLayoutManager);
        recyclerViewFavoritos.setAdapter(noticiasAdapter);

        textViewNombreDeLaCategoria.setText(nombreDeLaCategoria);
        return view;
    }





}
