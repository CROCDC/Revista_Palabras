package com.example.tcr.revistapalabras.View.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.example.tcr.revistapalabras.Controler.ControlerNoticias;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.example.tcr.revistapalabras.View.Adapter.AgendaAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAgendaCultural extends Fragment {

    private AgendaAdapter agendaAdapter;

    private CarouselLayoutManager layoutManager;


    private LinearLayout linearLayoutContenedor;
    private RecyclerView recyclerView;

    private ImageView imageViewTwitter;
    private ImageView imageViewFacebook;
    private ImageView imageViewInstagram;
    private ImageView imageViewPublicidad4;


    private I_NotificadorHaciaMainActivity notificador;

    public FragmentAgendaCultural() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        notificador = (I_NotificadorHaciaMainActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agendaAdapter = new AgendaAdapter(new AgendaAdapter.NotificadorHaciaImplementadorDelAdapter() {
            @Override
            public void notificar(Noticia noticia) {
                notificador.notificar(noticia);
            }

            @Override
            public void notificarTouchRedSocial(Integer numero) {
                notificador.notificarTouchRedSocial(numero);
            }

            @Override
            public void notificarTouchPublicidad(String link) {
                notificador.notificarTouchPublicidad(link);
            }
        });
        new ControlerNoticias().pedirListaDeNoticiassDeLaAgenda(1, 9, new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                Helper.setearImagenesAUnaListaDeContenidoDeLaAgenda(agendaAdapter, resultado);
                agendaAdapter.setListaDeItems(resultado);
                agendaAdapter.agregarFooter();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agenda_cultural, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAgenda_fragmentagendacultural);

        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(agendaAdapter);



        return view;
    }


}
