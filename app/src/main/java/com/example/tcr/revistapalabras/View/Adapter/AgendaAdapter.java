package com.example.tcr.revistapalabras.View.Adapter;

import android.content.Context;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter {
    private List<Noticia> listaDeNoticia;
    private Context context;
    private NotificadorHaciaImplementadorDelAdapter notificador;

    public AgendaAdapter(NotificadorHaciaImplementadorDelAdapter notificador) {
        this.notificador = notificador;
        listaDeNoticia = new ArrayList<>();
    }

    public void setListaDeNoticia(List<Noticia> listaDeNoticia) {
        this.listaDeNoticia = listaDeNoticia;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View viewDeLaCelda = layoutInflater.inflate(R.layout.celda_agenda, parent, false);

        AgendaViewHolder agendaViewHolder = new AgendaViewHolder(viewDeLaCelda);
        return agendaViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Noticia noticia = listaDeNoticia.get(position);

        AgendaViewHolder agendaViewHolder = (AgendaViewHolder) holder;

        agendaViewHolder.cargarAgenda(noticia);
    }

    @Override
    public int getItemCount() {
        return listaDeNoticia.size();
    }


    private class AgendaViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewDeLaNota;
        private TextView textViewTituloDeLaNota;
        private TextView textViewPreviewDeLaNota;

        public AgendaViewHolder(View itemView) {
            super(itemView);


            imageViewDeLaNota = itemView.findViewById(R.id.imageViewDeLaNota_celdaagenda);
            textViewTituloDeLaNota = itemView.findViewById(R.id.textViewTituloDeLaNota_celdaagenda);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificador.notificar(listaDeNoticia.get(getAdapterPosition()));
                }
            });


        }

        public void cargarAgenda(Noticia noticia) {
            try {
                Helper.cargarImagenesAgenda(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium().getSource_url());

            }catch (Exception e){

            }

            textViewTituloDeLaNota.setText(noticia.getTitle().getRendered());

        }
    }
    public interface NotificadorHaciaImplementadorDelAdapter {
        public void notificar(Noticia noticia);
    }
}
