package com.ar.tcr.revistapalabras.View.Fragments;

import com.ar.tcr.revistapalabras.Model.Noticia;

public interface I_NotificadorHaciaMainActivity {
    void notificar(Noticia noticia);
    void notificarTouchPublicidad(String link);
    void notificarTouchRedSocial(Integer numero);
    void notificarSinResultados();
}
