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
import android.widget.TextView;

import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Adapter.NoticiasAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotasPorCategoria extends Fragment {

    public static final String CLAVE_NOMBRE_DE_LA_CATEGORIA = "nombre de la categoriaaaaa";

    private static NoticiasAdapter noticiasAdapter;
    private RecyclerView recyclerViewCategorias;
    private static ControlerNoticias controlerNoticias;
    private TextView textViewNombreDeLaCategoria;
    private static Boolean estaCargando;
    private static Integer categoriaGlobal;

    private static NotificadorHaciaMainActivity notificador;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        notificador = (NotificadorHaciaMainActivity) context;
    }
    public static FragmentNotasPorCategoria fabricaDeFragmentPorCategoria(Integer categoria, String nombreDeLaCategoria) {

        categoriaGlobal = categoria;

        Bundle bundle = new Bundle();

        bundle.putString(CLAVE_NOMBRE_DE_LA_CATEGORIA, nombreDeLaCategoria);

        FragmentNotasPorCategoria fragmentNotasPorCategoria = new FragmentNotasPorCategoria();

        fragmentNotasPorCategoria.setArguments(bundle);
        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificar(Noticia unNoticia) {
                notificador.notificar(unNoticia);
            }
        });

        controlerNoticias = new ControlerNoticias();
        controlerNoticias.pedirListaDeNoticiasPorCategoria(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(final List<Noticia> resultado) {

                noticiasAdapter.setListaDeNoticias(resultado);
                estaCargando = false;
            }


        }, categoria);

        return fragmentNotasPorCategoria;
    }


    public FragmentNotasPorCategoria() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notas_por_categoria, container, false);

        Bundle bundle = getArguments();

        String nombreDeLaCategoria = bundle.getString(CLAVE_NOMBRE_DE_LA_CATEGORIA);

        textViewNombreDeLaCategoria = view.findViewById(R.id.textViewNombreDeLaCategoria_fragmentnotasporcategoria);
        recyclerViewCategorias = view.findViewById(R.id.recyclerViewCategorias_fragmentnotasporcategoria);

        recyclerViewCategorias.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation);

        recyclerViewCategorias.setLayoutAnimation(layoutAnimationController);

        recyclerViewCategorias.scheduleLayoutAnimation();

        recyclerViewCategorias.setLayoutManager(linearLayoutManager);
        recyclerViewCategorias.setAdapter(noticiasAdapter);

        textViewNombreDeLaCategoria.setText(nombreDeLaCategoria);


        recyclerViewCategorias.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    },categoriaGlobal);
                }
            }
        });

        return view;
    }

    public interface NotificadorHaciaMainActivity {
        public void notificar(Noticia noticia);
    }
}
