package com.ar.tcr.revistapalabras.Controler;

import com.ar.tcr.revistapalabras.DAO.DAOPublicidadesFirebase;
import com.ar.tcr.revistapalabras.Model.Publicidad;
import com.ar.tcr.revistapalabras.Utils.ResultListener;

import java.util.List;

public class ControllerPublicidadesFirebase {



    public void traerListaDePublicidades(final ResultListener<List<Publicidad>> escuchadorDeLaVista){
        new DAOPublicidadesFirebase().traerListaDePublicidades(new ResultListener<List<Publicidad>>() {
            @Override
            public void finish(List<Publicidad> resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });
    }
}
