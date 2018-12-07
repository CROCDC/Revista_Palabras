package com.ar.tcr.revistapalabras.Controler;

import com.ar.tcr.revistapalabras.DAO.DAONoticiaFavoritoFirebase;
import com.ar.tcr.revistapalabras.Model.Noticia;
import com.ar.tcr.revistapalabras.Utils.ResultListener;

import java.util.ArrayList;
import java.util.List;

public class ControlerContenidoFavoritoFirebase {

    public void pedirListaDeNoticiasFavoritas(final ResultListener<List<Noticia>> escuchadorDeLaVista) {

        final List<Noticia> listaDeNoticiaAlReves = new ArrayList<>();

        new DAONoticiaFavoritoFirebase().pedirListaDeNoticiasFavoritas(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {

                for (int i = resultado.size() - 1; i >= 0; i--) {

                    listaDeNoticiaAlReves.add(resultado.get(i));
                }

                escuchadorDeLaVista.finish(listaDeNoticiaAlReves);
            }
        });
    }

    public void verficiarSiLaNoticiaEstaEnFirebase(Noticia noticia,final ResultListener<Boolean> escuchadorDeLaVista){

        new DAONoticiaFavoritoFirebase().verficiarSiLaNoticiaEstaEnFirebase(noticia, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });

    }
    public void agregarLaNoticiaAGuardado(Noticia noticia,final ResultListener<Boolean> escuchadorDeLaVista){
        new DAONoticiaFavoritoFirebase().agregarLaNoticiaAGuardado(noticia, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });
    }
}
