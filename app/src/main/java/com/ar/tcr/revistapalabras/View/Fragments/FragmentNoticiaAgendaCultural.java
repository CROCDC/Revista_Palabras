package com.ar.tcr.revistapalabras.View.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.ar.tcr.revistapalabras.Model.Noticia;
import com.ar.tcr.revistapalabras.R;
import com.ar.tcr.revistapalabras.Utils.Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNoticiaAgendaCultural extends Fragment {
    private ImageView imageViewDeLaNoticia;
    private TextView textViewTituloDeLaNoticia;
    private Noticia noticia;
    private CarouselLayoutManager layoutManager;


    public static final String CLAVE_OBJETO_AGENDA = "id";

    public static FragmentNoticiaAgendaCultural fabricaDeFragmentsNoticiaAgendaCultural(Noticia noticia) {
        FragmentNoticiaAgendaCultural fragmentNoticiaAgendaCultural = new FragmentNoticiaAgendaCultural();

        Bundle bundle = new Bundle();

        bundle.putSerializable(CLAVE_OBJETO_AGENDA, noticia);

        fragmentNoticiaAgendaCultural.setArguments(bundle);

        return fragmentNoticiaAgendaCultural;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        noticia = (Noticia) bundle.getSerializable(CLAVE_OBJETO_AGENDA);

        Helper.cargarImagenes(imageViewDeLaNoticia, getContext(), noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium().getSource_url());

        textViewTituloDeLaNoticia.setText(noticia.getTitle().getRendered());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noticia_agenda_cultural, container, false);

        imageViewDeLaNoticia = view.findViewById(R.id.imageViewDeLaNota_fragmentnoticiaagendacultural);
        textViewTituloDeLaNoticia = view.findViewById(R.id.textViewTituloDeLaNota_fragmentnoticiaagendacultural);

        return view;
    }

}
