package com.example.tcr.revistapalabras.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;


import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
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
    private NotificadorHaciaMainActivity notificador;
    private ProgressBar progressBar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.notificador = (NotificadorHaciaMainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controlerNoticias = new ControlerNoticias();
        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificar(Noticia unNoticia) {
                notificador.notificar(unNoticia);
            }
        });

        controlerNoticias.pedirListaDeLasUltimasNoticiasPublicadas(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(final List<Noticia> resultado) {
                noticiasAdapter.setListaDeNoticias(resultado);
                isLoading = false;
            }


        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dentro_del_main_activty, container, false);


        recyclerViewNotas = view.findViewById(R.id.recyclerViewNotas_fragmentdentrodelmainactivity);


        recyclerViewNotas.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewNotas.setLayoutManager(linearLayoutManager);

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation);

        recyclerViewNotas.setLayoutAnimation(layoutAnimationController);

        recyclerViewNotas.scheduleLayoutAnimation();

        recyclerViewNotas.setAdapter(noticiasAdapter);

        recyclerViewNotas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isLoading) {
                    return;
                }

                Integer posicionActual = linearLayoutManager.findLastVisibleItemPosition();
                Integer ultimaCelda = linearLayoutManager.getItemCount();

                if (posicionActual >= (ultimaCelda - 2)) {
                    isLoading = true;
                    controlerNoticias.pedirListaDeLasUltimasNoticiasPublicadas(new ResultListener<List<Noticia>>() {
                        @Override
                        public void finish(final List<Noticia> resultado) {

                            isLoading = false;
                            noticiasAdapter.agregarNotasALaLista(resultado);

                        }


                    });
                }
            }
        });
        return view;
    }

    public interface NotificadorHaciaMainActivity {
        void notificar(Noticia unaNoticia);
    }
}
