package com.example.tcr.revistapalabras.View.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.Model.Imagen;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class NoticiasAdapter extends RecyclerView.Adapter {
    private List<Noticia> listaDeNoticias;
    private List<Imagen> rutasDeImagenes;

    private Context context;
    private Notificador notificador;

    public int lastPosition;

    public void setListaDeNoticias(List<Noticia> listaDeNoticias) {
        this.listaDeNoticias = listaDeNoticias;
        notifyDataSetChanged();
    }

    public void agregarNotasALaLista(List<Noticia> listaDeNotasAgregar) {
        this.listaDeNoticias.addAll(listaDeNotasAgregar);
        notifyDataSetChanged();
    }

    public void agregarAListaDeNotas(Noticia noticia) {
        listaDeNoticias.add(noticia);
        notifyDataSetChanged();
    }

    public void setRutaDeImagen(List<Imagen> rutaDeImagen) {
        this.rutasDeImagenes = rutaDeImagen;
        notifyDataSetChanged();
    }

    public void agregarRutaDeImagen(Imagen rutaDeImagen) {
        rutasDeImagenes.add(rutaDeImagen);
        notifyDataSetChanged();
    }

    public NoticiasAdapter(Notificador notificador) {
        listaDeNoticias = new ArrayList<>();
        rutasDeImagenes = new ArrayList<>();
        this.notificador = notificador;
        lastPosition = -1;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View viewDeLaCelda = layoutInflater.inflate(R.layout.celda_nota, parent, false);

        NotasViewHolder notasViewHolder = new NotasViewHolder(viewDeLaCelda);
        return notasViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Noticia noticia = listaDeNoticias.get(position);

        NotasViewHolder notasViewHolder = (NotasViewHolder) holder;


        //animacion de entrada de items
        if (position > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.entrada_por_arriba);
            notasViewHolder.itemView.startAnimation(animation);
            lastPosition = position;

        }
        notasViewHolder.cargarNoticia(noticia);


    }

    @Override
    public int getItemCount() {
        return listaDeNoticias.size();
    }

    private class NotasViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewDeLaNota;
        private TextView textViewTituloDeLaNota;
        private TextView textViewPreviewDeLaNota;

        public NotasViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificador.notificar(listaDeNoticias.get(getAdapterPosition()));
                }
            });

            imageViewDeLaNota = itemView.findViewById(R.id.imageViewDeLaNota_celdanota);
            textViewTituloDeLaNota = itemView.findViewById(R.id.textViewTituloDeLaNota_celdanota);
            textViewPreviewDeLaNota = itemView.findViewById(R.id.textViewDescripcionDeLaNota_celdanota);

            imageViewDeLaNota.setDrawingCacheEnabled(false);
        }

        public void cargarNoticia(final Noticia noticia) {

            try {
                Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium().getSource_url());

            }catch (Exception e){
                try {
                    Helper.cargarImagenes(imageViewDeLaNota,context,noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium().getSource_url());

                }catch (Exception e1){
                    Helper.cargarImagenes(imageViewDeLaNota,context,noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getThumbnail().getSource_url());

                }
            }


            textViewTituloDeLaNota.setText(noticia.getTitle().getRendered());

            textViewPreviewDeLaNota.setText(noticia.getExcerpt().getRendered());


        }


    }


    public interface Notificador {
        void notificar(Noticia unaNoticia);
    }
}
