package com.example.tcr.revistapalabras.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
import com.example.tcr.revistapalabras.Model.Footer;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Adapter.NoticiasAdapter;

import java.util.List;


public class FragmentUltimasNoticias extends Fragment {

    private Boolean isLoading = false;

    private RecyclerView recyclerViewNotas;
    private LinearLayoutManager linearLayoutManager;
    private NoticiasAdapter noticiasAdapter;
    private ControlerNoticias controlerNoticias;
    private I_NotificadorHaciaMainActivity notificador;
    private LinearLayout linearLayoutContenedorRedesSociales;
    private ProgressBar progressBar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.notificador = (I_NotificadorHaciaMainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controlerNoticias = new ControlerNoticias();
        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificarTouchCelda(Noticia unNoticia) {
                notificador.notificar(unNoticia);
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

        controlerNoticias.pedirListaDeLasUltimasNoticiasPublicadas(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(final List<Noticia> resultado) {
                noticiasAdapter.setListaDeNoticias(resultado);
                isLoading = false;
                progressBar.setVisibility(View.INVISIBLE);
            }


        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ultimas_noticias, container, false);


        recyclerViewNotas = view.findViewById(R.id.recyclerViewNotas_fragmentdentrodelmainactivity);
        progressBar = view.findViewById(R.id.progressBar_fragmentultimasnoticias);


        recyclerViewNotas.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewNotas.setLayoutManager(linearLayoutManager);

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation);

        recyclerViewNotas.setLayoutAnimation(layoutAnimationController);

        recyclerViewNotas.scheduleLayoutAnimation();

        recyclerViewNotas.setAdapter(noticiasAdapter);

        recyclerViewNotas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isLoading) {
                    return;
                }

                Integer posicionActual = linearLayoutManager.findLastVisibleItemPosition();
                Integer ultimaCelda = linearLayoutManager.getItemCount();

                if (posicionActual >= (ultimaCelda - 2)) {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    controlerNoticias.pedirListaDeLasUltimasNoticiasPublicadas(new ResultListener<List<Noticia>>() {
                        @Override
                        public void finish(final List<Noticia> resultado) {


                            if (resultado.isEmpty()){
                                progressBar.setVisibility(View.INVISIBLE);
                                noticiasAdapter.agregarFooter(new Footer());
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                isLoading = false;
                                noticiasAdapter.agregarNotasALaLista(resultado);
                            }




                        }


                    });
                }
            }
        });

        return view;


    }




}
