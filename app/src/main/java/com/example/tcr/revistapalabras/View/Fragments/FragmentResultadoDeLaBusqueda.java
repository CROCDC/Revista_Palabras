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

import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Adapter.NoticiasAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentResultadoDeLaBusqueda extends Fragment {


    public static final String CLAVE_BUSQUEDA = "busqueda";

    private RecyclerView recyclerViewListaDeResultadosDeLaBusqueda;

    private NoticiasAdapter noticiasAdapter;

    private NotificadorHaciaMainActivity notificador;

    public FragmentResultadoDeLaBusqueda() {
        // Required empty public constructor
    }

    public static FragmentResultadoDeLaBusqueda fabricaDeFragmentsResultadoDeLaBusqueda(String busqueda){
        FragmentResultadoDeLaBusqueda fragmentResultadoDeLaBusqueda = new FragmentResultadoDeLaBusqueda();

        Bundle bundle = new Bundle();

        bundle.putString(CLAVE_BUSQUEDA,busqueda);

        fragmentResultadoDeLaBusqueda.setArguments(bundle);

        return fragmentResultadoDeLaBusqueda;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        notificador = (NotificadorHaciaMainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        String busqueda = bundle.getString(CLAVE_BUSQUEDA);

        noticiasAdapter = new NoticiasAdapter(new NoticiasAdapter.Notificador() {
            @Override
            public void notificar(Noticia unaNoticia) {
                notificador.notificar(unaNoticia);
            }
        });

        new ControlerNoticias().pedirListaDeNoticiasDeLaBusqueda(busqueda, new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                noticiasAdapter.setListaDeNoticias(resultado);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_resultado_de_la_busqueda, container, false);

        recyclerViewListaDeResultadosDeLaBusqueda = view.findViewById(R.id.recyclerViewListaDeResultadoDeLaBusqueda_fragmentresultadodelabusqueda);

        recyclerViewListaDeResultadosDeLaBusqueda.setAdapter(noticiasAdapter);

        recyclerViewListaDeResultadosDeLaBusqueda.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        return view;
    }

    public interface NotificadorHaciaMainActivity {
        public void notificar(Noticia noticia);
    }
}
