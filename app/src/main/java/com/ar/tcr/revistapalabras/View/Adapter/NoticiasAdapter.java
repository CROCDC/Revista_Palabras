package com.ar.tcr.revistapalabras.View.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ar.tcr.revistapalabras.Controler.ControllerPublicidadesFirebase;
import com.ar.tcr.revistapalabras.Model.Footer;
import com.ar.tcr.revistapalabras.Model.Imagen;
import com.ar.tcr.revistapalabras.Model.Noticia;
import com.ar.tcr.revistapalabras.Model.Publicidad;
import com.ar.tcr.revistapalabras.Model.RecyclerViewItem;
import com.ar.tcr.revistapalabras.R;
import com.ar.tcr.revistapalabras.Utils.Helper;
import com.ar.tcr.revistapalabras.Utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class NoticiasAdapter extends RecyclerView.Adapter {
    private List<Noticia> listaDeNoticias;
    private List<Imagen> rutasDeImagenes;

    private Context context;
    private Notificador notificador;
    private NotasViewHolder notasViewHolder;
    private List<RecyclerViewItem> listaItems = new ArrayList<>();

    public static final int TIPO_NOTICIA = 0;
    public static final int TIPO_FOOTER = 1;

    public int lastPosition;

    public void setListaDeNoticias(List<Noticia> listaDeNoticias) {

        listaItems.addAll(listaDeNoticias);
        notifyDataSetChanged();
    }

    public void agregarNotasALaLista(List<Noticia> listaDeNotasAgregar) {
        listaItems.addAll(listaDeNotasAgregar);
        notifyDataSetChanged();
    }

    public void agregarFooter(Footer footer) {
        listaItems.add(footer);
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
        View viewDeLaCelda;


        if (viewType == TIPO_NOTICIA) {

            viewDeLaCelda = layoutInflater.inflate(R.layout.celda_nota, parent, false);

            notasViewHolder = new NotasViewHolder(viewDeLaCelda);


            return notasViewHolder;
        } else if (viewType == TIPO_FOOTER) {
            viewDeLaCelda = layoutInflater.inflate(R.layout.footer_recyclerview,parent,false);


            return new FooterViewHolder(viewDeLaCelda);


        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NotasViewHolder){
            Noticia noticia = (Noticia) listaItems.get(position);


            NotasViewHolder notasViewHolder = (NotasViewHolder) holder;


            //animacion de entrada de items
            if (position > lastPosition) {

                Animation animation = AnimationUtils.loadAnimation(context,
                        R.anim.entrada_por_arriba);
                notasViewHolder.itemView.startAnimation(animation);
                lastPosition = position;

            }
            notasViewHolder.cargarNoticia(noticia);
        }else {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;


        }





    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        RecyclerViewItem recyclerViewItem = listaItems.get(position);

        if (recyclerViewItem instanceof Noticia) {
            return TIPO_NOTICIA;

        } else if (recyclerViewItem instanceof Footer) {
            return TIPO_FOOTER;
        } else {
            return super.getItemViewType(position);
        }

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
                    notificador.notificarTouchCelda((Noticia) listaItems.get(getAdapterPosition()));
                }
            });

            imageViewDeLaNota = itemView.findViewById(R.id.imageViewDeLaNota_celdanota);
            textViewTituloDeLaNota = itemView.findViewById(R.id.textViewTituloDeLaNota_celdanota);
            textViewPreviewDeLaNota = itemView.findViewById(R.id.textViewDescripcionDeLaNota_celdanota);

            imageViewDeLaNota.setDrawingCacheEnabled(false);
        }

        public void cargarNoticia(final Noticia noticia) {

            try {
                Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getFull().getSource_url());

            } catch (Exception e) {
                try {
                    Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getLarge().getSource_url());

                } catch (Exception e1) {
                    try {
                        Helper.cargarImagenes(imageViewDeLaNota, context, noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium_large().getSource_url());

                    }catch (Exception e2){
                        Helper.cargarImagenes(imageViewDeLaNota,context,noticia.getImagen());
                    }

                }
            }


            try {
                textViewTituloDeLaNota.setText(Html.fromHtml(noticia.getTitle().getRendered()));
                textViewPreviewDeLaNota.setText(Html.fromHtml(noticia.getExcerpt().getRendered()));
            }catch (Exception expec){
                textViewTituloDeLaNota.setText(Html.fromHtml(noticia.getTitleS()));
                textViewPreviewDeLaNota.setText(Html.fromHtml(noticia.getPreview()));
            }



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
                    cargarPublicidad(resultado.get(2).getUrl(),imageViewPublicidad3);

                    imageViewPublicidad3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notificador.notificarTouchPublicidad(resultado.get(2).getLink());
                        }
                    });
                }
            });

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


    public interface Notificador {
        void notificarTouchCelda(Noticia unaNoticia);
        void notificarTouchPublicidad(String link);
        void notificarTouchRedSocial(Integer numero);
    }
}
