package com.example.tcr.revistapalabras.Utils;

import com.example.tcr.revistapalabras.Model.Imagen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceImagenes {
    @GET("wp/v2/media/{idNota}")
    Call<Imagen> pedirImagenPorID(
            @Path("idNota") int idNota
    );
}
