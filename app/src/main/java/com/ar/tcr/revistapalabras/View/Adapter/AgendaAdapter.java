package com.ar.tcr.revistapalabras.View.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ar.tcr.revistapalabras.Controler.ControllerPublicidadesFirebase;
import com.ar.tcr.revistapalabras.Model.Footer;
import com.ar.tcr.revistapalabras.Model.Noticia;
import com.ar.tcr.revistapalabras.Model.Publicidad;
import com.ar.tcr.revistapalabras.Model.RecyclerViewItem;
import com.ar.tcr.revistapalabras.R;
import com.ar.tcr.revistapalabras.Utils.Helper;
import com.ar.tcr.revistapalabras.Utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter {
    private List<RecyclerViewItem> listaDeItems;
    private Context context;
    private NotificadorHaciaImplementadorDelAdapter notificador;


    public static final int TIPO_NOTICIA = 0;
    public static final int TIPO_FOOTER = 1;
    public static final int TIPO_PUBLICIDAD =2;

    public AgendaAdapter(NotificadorHaciaImplementadorDelAdapter notificador) {
        this.notificador = notificador;
        listaDeItems = new ArrayList<>();
    }

    public void setListaDeItems(List<Noticia> listaDeNoticias) {
        listaDeItems.addAll(listaDeNoticias);
        agregerPublicidad();
        notifyDataSetChanged();
    }

    public void agregerPublicidad(){
        listaDeItems.add(5,new Publicidad());
    }

    public void agregarFooter() {
        listaDeItems.add(new Footer());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewDeLaCelda;

        if (viewType == TIPO_NOTICIA) {
            viewDeLaCelda = layoutInflater.inflate(R.layout.celda_agenda, parent, false);

            AgendaViewHolder agendaViewHolder = new AgendaViewHolder(viewDeLaCelda);
            return agendaViewHolder;
        }else if (viewType == TIPO_FOOTER){

            viewDeLaCelda = layoutInflater.inflate(R.layout.footer_recyclerview,parent,false);

            return new FooterViewHolder(viewDeLaCelda);

        }else if (viewType == TIPO_PUBLICIDAD){
            viewDeLaCelda = layoutInflater.inflate(R.layout.publicidad,parent,false);

            return new PublicidadViewHolder(viewDeLaCelda);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AgendaViewHolder){

            Noticia noticia = (Noticia) listaDeItems.get(position);

            AgendaViewHolder agendaViewHolder = (AgendaViewHolder) holder;

            agendaViewHolder.cargarAgenda(noticia);
        }else if (holder instanceof FooterViewHolder){
          FooterViewHolder footerViewHolder = (FooterViewHolder) holder;

        }else if (holder instanceof PublicidadViewHolder){
            PublicidadViewHolder publicidadViewHolder = (PublicidadViewHolder) holder;
        }

    }

    @Override
    public int getItemCount() {
        return listaDeItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        RecyclerViewItem recyclerViewItem = listaDeItems.get(position);

        if (recyclerViewItem instanceof Noticia) {
            return TIPO_NOTICIA;
        } else if (recyclerViewItem instanceof Footer) {
            return TIPO_FOOTER;
        } else if (recyclerViewItem instanceof Publicidad){
            return TIPO_PUBLICIDAD;
        }

        else {
            return super.getItemViewType(position);

        }

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
                    notificador.notificar((Noticia) listaDeItems.get(getAdapterPosition()));
                }
            });


        }

        public void cargarAgenda(Noticia noticia) {
            try {
                Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getFull().getSource_url());

            } catch (Exception e) {
                try {
                    Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getLarge().getSource_url());

                } catch (Exception e1) {
                    Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium_large().getSource_url());

                }
            }

            textViewTituloDeLaNota.setText(noticia.getTitle().getRendered());

        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewTwitter;
        private ImageView imageViewFacebook;
        private ImageView imageViewInstagram;
        private ImageView imageViewPublicidad3;


        public FooterViewHolder(View itemView) {
            super(itemView);
            imageViewTwitter = itemView.findViewById(R.id.buttonImageViewTwitter_footer);
            imageViewFacebook = itemView.findViewById(R.id.buttonImageViewFacebook_footer);
            imageViewInstagram = itemView.findViewById(R.id.buttonImageViewInstagram_footer);
            imageViewPublicidad3 = itemView.findViewById(R.id.imageViewPublicidad3_footer);


            imageViewTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificador.notificarTouchRedSocial(Helper.REFERENCIA_TWITTER);
                }
            });

            imageViewFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificador.notificarTouchRedSocial(Helper.REFERENCIA_FACEBOOK);
                }
            });

            imageViewInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificador.notificarTouchRedSocial(Helper.REFERENCIA_INSTAGRAM);
                }
            });


            new ControllerPublicidadesFirebase().traerListaDePublicidades(new ResultListener<List<Publicidad>>() {
                @Override
                public void finish(final List<Publicidad> resultado) {
                    cargarPublicidad(resultado.get(3).getUrl(),imageViewPublicidad3);

                    imageViewPublicidad3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notificador.notificarTouchPublicidad(resultado.get(3).getLink());
                        }
                    });
                }
            });

        }




    }

    private class PublicidadViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewPublicidad5;
        public PublicidadViewHolder(View itemView) {
            super(itemView);

            imageViewPublicidad5 = itemView.findViewById(R.id.imageViewPublicidad5_publicidad);

            new ControllerPublicidadesFirebase().traerListaDePublicidades(new ResultListener<List<Publicidad>>() {
                @Override
                public void finish(final List<Publicidad> resultado) {
                    cargarPublicidad(resultado.get(4).getUrl(),imageViewPublicidad5);

                    imageViewPublicidad5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notificador.notificarTouchPublicidad(resultado.get(4).getLink());
                        }
                    });
                }
            });

        }
    }

    public interface NotificadorHaciaImplementadorDelAdapter {
        public void notificar(Noticia noticia);
        public void notificarTouchRedSocial(Integer numero);
        public void notificarTouchPublicidad(String link);
    }

    public void cargarPublicidad(String url, ImageView imageView) {

        if (url.equals("")) {
            return;
        }

        if (url.endsWith(".gif")) {
            Glide.with(context)
                    .load(url)
                    .asGif()
                    .crossFade()
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .into(imageView);
        }
    }

}
