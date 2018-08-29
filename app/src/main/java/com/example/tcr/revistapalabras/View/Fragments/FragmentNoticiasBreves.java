package com.example.tcr.revistapalabras.View.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Activitys.DescripcionesDeNotasActivity;
import com.example.tcr.revistapalabras.View.Adapter.NoticiasAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNoticiasBreves extends Fragment {

    private RecyclerView recyclerViewListaDeNoticiasBreves;

    private NoticiasAdapter noticiasAdapter;

    private NotificadorHaciaMainActivity notificador;

    private Boolean estaCargando;

    private LinearLayoutManager linearLayoutManager;

    private ControlerNoticias controlerNoticias;

    public FragmentNoticiasBreves() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        notificador = (NotificadorHaciaMainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controlerNoticias = new ControlerNoticias();

        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificar(Noticia unaNoticia) {
                notificador.notificar(unaNoticia);

            }
        });
        controlerNoticias.pedirListaDeNoticiasPorCategoria(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                noticiasAdapter.setListaDeNoticias(resultado);
                estaCargando = false;
            }
        }, Helper.BREVES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticias_breves, container, false);

        recyclerViewListaDeNoticiasBreves = view.findViewById(R.id.recyclerViewListaDeNoticiasBreves_fragmentnoticiasbreves);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewListaDeNoticiasBreves.setLayoutManager(linearLayoutManager);

        recyclerViewListaDeNoticiasBreves.setAdapter(noticiasAdapter);

        recyclerViewListaDeNoticiasBreves.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (estaCargando) {
                    return;
                }

                Integer posicionActual = linearLayoutManager.findLastVisibleItemPosition();
                Integer ultimaCelda = linearLayoutManager.getItemCount();

                if (posicionActual >= (ultimaCelda - 2)) {

                    estaCargando = true;
                    controlerNoticias.pedirListaDeNoticiasPorCategoria(new ResultListener<List<Noticia>>() {
                        @Override
                        public void finish(List<Noticia> resultado) {
                            estaCargando = false;
                            noticiasAdapter.agregarNotasALaLista(resultado);
                        }
                    }, Helper.BREVES);
                }
            }
        });

        return view;
    }

    public interface NotificadorHaciaMainActivity {
        public void notificar(Noticia noticia);
    }

}
