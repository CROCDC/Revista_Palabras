package com.example.tcr.revistapalabras.View.Fragments;

import com.example.tcr.revistapalabras.Model.Noticia;

public interface I_NotificadorHaciaMainActivity {
    void notificar(Noticia noticia);
    void notificarTouchPublicidad(String link);
    void notificarTouchRedSocial(Integer numero);
    void notificarSinResultados();
}
