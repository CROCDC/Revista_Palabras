package com.example.tcr.revistapalabras.Utils;
/*
  Creado por Camilo 05/06/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tcr.revistapalabras.Controler.ControlerImagenes;
import com.example.tcr.revistapalabras.Model.Excerpt;
import com.example.tcr.revistapalabras.Model.Imagen;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static final String urlBase = "http://www.palabras.com.ar/wp-json/";

    public static final String urlAPINotification = "http://www.palabras.com.ar/pnfw/register/";

    public static final int BREVES = 44;

    public static final int AMERICA = 53;

    public static final int ANDROID = 10;

    public static final int LITERATURA = 32;

    public static final int IDEAS = 32;

    public static final int FOTOGRAFIAS = 39;

    public static final int ARQUITECTURA = 57;

    public static final int ARTESESCENICAS = 49;

    public static final int AUDIOVISUALES = 33;

    public static final int BALANCESYPERSPECTIVAS = 50;

    public static final int BALLET = 35;

    public static final int CINEYSERIES = 3;

    public static final int AGENDA = 34;

    public static final String REFERENCIA_CONTENIDO_FAVORITO = "Notas Favoritas";

    public static final String REFERENCIA_PUBLICIDADES ="publicidades";

    public static final int REFERENCIA_TWITTER = 0;

    public static final int REFERENCIA_FACEBOOK = 1;

    public static final int REFERENCIA_INSTAGRAM = 2;



    //Removedor de etiquetas HMTL
    public static String eliminarEtiquetasHTML(String html) {
        return Jsoup.parse(html).text();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void acomodarPreviewDeNotasPorLista(List<Noticia> listaDeNoticia) {
        for (Integer i = 0; i < listaDeNoticia.size(); i++) {

            Excerpt excerpt = listaDeNoticia.get(i).getExcerpt();

            excerpt.setRendered(excerpt.getRendered().replace(excerpt.getRendered().substring(0, 3), ""));

            Integer tamañoDeLaPreview = listaDeNoticia.get(i).getExcerpt().getRendered().length();
            excerpt.setRendered(excerpt.getRendered().replace(excerpt.getRendered().substring(tamañoDeLaPreview - 15, tamañoDeLaPreview), "..."));

        }
    }

    public static void acomodarPreviewDeNotasEnSingular(Noticia noticia) {

        Excerpt excerpt = noticia.getExcerpt();

        excerpt.setRendered(excerpt.getRendered().replace(excerpt.getRendered().substring(0, 3), ""));

        Integer tamañoDeLaPreview = noticia.getExcerpt().getRendered().length();
        excerpt.setRendered(excerpt.getRendered().replace(excerpt.getRendered().substring(tamañoDeLaPreview - 15, tamañoDeLaPreview), "..."));
    }

    public static ArrayList invertirElOrdenDeUnaLista(ArrayList list) {
        ArrayList arrayListInvertida = new ArrayList();

        for (int i = list.size() - 1; i >= 0; i--) {

            arrayListInvertida.add(list.get(i));
        }

        return arrayListInvertida;

    }

    public static List<Noticia> eliminarNoticiasBrevesYAgenda(List<Noticia> listaDeNoticias) {
        List<Noticia> listaDeNoticiasARemover = new ArrayList<>();


        for (Integer i = 0; i < listaDeNoticias.size(); i++) {

            for (Integer j = 0; j < listaDeNoticias.get(i).getCategories().size(); j++) {

                if (listaDeNoticias.get(i).getCategories().get(j).equals(Helper.AGENDA) || listaDeNoticias.get(i).getCategories().get(j).equals(Helper.BREVES)) {

                    listaDeNoticiasARemover.add(listaDeNoticias.get(i));

                }
            }
        }

        listaDeNoticias.removeAll(listaDeNoticiasARemover);

        return listaDeNoticias;
    }


    public static void cargarImagenes(ImageView imageView, Context context, String url) {
        Picasso.with(context)
                .load(url)
                .placeholder(R.color.blanco)
                .into(imageView);
    }

    public static void setearImagenesAUnaListaDeContenidoDeLaAgenda(final RecyclerView.Adapter adapter, List<Noticia> listaDeNoticia) {
        /*
        Juan si ves esto te juro que lo puedo explicar x
         */


        for (final Noticia noticia : listaDeNoticia) {
            new ControlerImagenes().pedirImagenPorid(noticia.getFeatured_media(), new ResultListener<Imagen>() {
                @Override
                public void finish(Imagen imagen) {
                    noticia.setImagen(imagen.getMedia_details().getSizes().getThumbnail().getSource_url());
                    adapter.notifyDataSetChanged();

                }

            });

        }
    }

    public static void cargarImagenesAgenda(ImageView imageView, Context context, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.color.blanco)
                .into(imageView);
    }


}




