package com.ar.tcr.revistapalabras.Utils;

import com.ar.tcr.revistapalabras.Model.Noticia;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
  Creado por Camilo 05/06/2018.
 */

public interface ServiceNoticia {

    @GET("wp/v2/posts?_embed")
    Call<List<Noticia>> pedirListaDeNoticias(
            @Query("page") int pagina
    );

    @GET("wp/v2/posts?_embed")
    Call<List<Noticia>> pedirListaDeNoticiasDeLaBusqueda(
            @Query("search") String busqueda
    );

    @GET("wp/v2/posts?_embed")
    Call<List<Noticia>> pedirListaDeNoticiasPorCategoria(
            @Query("categories") Integer idcategoria,
            @Query("page") int pagina,
            @Query("per_page") int tamaño

    );
    @GET("wp/v2/posts/{id_nota}?_embed")
    Call<Noticia> pedirUnaNoticiaPorID(
            @Path("id_nota") int pagina
    );

}
