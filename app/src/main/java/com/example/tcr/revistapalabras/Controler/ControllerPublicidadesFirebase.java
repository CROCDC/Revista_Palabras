package com.example.tcr.revistapalabras.Controler;

import com.example.tcr.revistapalabras.DAO.DAOPublicidadesFirebase;
import com.example.tcr.revistapalabras.Model.Publicidad;
import com.example.tcr.revistapalabras.Utils.ResultListener;

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
