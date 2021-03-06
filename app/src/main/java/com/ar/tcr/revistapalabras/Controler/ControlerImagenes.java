package com.ar.tcr.revistapalabras.Controler;

import android.graphics.Bitmap;

import com.ar.tcr.revistapalabras.DAO.DAOImagenesRetrofit;
import com.ar.tcr.revistapalabras.Model.Imagen;
import com.ar.tcr.revistapalabras.Utils.ResultListener;

public class ControlerImagenes {
    public void pedirImagenPorid(Integer idNota, final ResultListener<Imagen> escuchadorDeLaVista){
        new DAOImagenesRetrofit().pedirDeImagenPorid(idNota, new ResultListener<Imagen>() {
            @Override
            public void finish(Imagen resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });
    }
    public void imagenToBitmap(String src, final ResultListener<Bitmap> escuchadorDeLaVista){
        new DAOImagenesRetrofit().imagenToBitmap(src, new ResultListener<Bitmap>() {
            @Override
            public void finish(Bitmap resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });
    }
}
