package com.ar.tcr.revistapalabras.DAO;
/*
  Creado por Camilo 05/06/2018.
 */

import com.ar.tcr.revistapalabras.Model.Noticia;
import com.ar.tcr.revistapalabras.Utils.Helper;
import com.ar.tcr.revistapalabras.Utils.ResultListener;
import com.ar.tcr.revistapalabras.Utils.ServiceNoticia;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DAONoticiaRetrofit {
    private Retrofit retrofit;
    private ServiceNoticia serviceNoticia;

    public DAONoticiaRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.urlBase)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.client(httpClient.build()).build();
    }

    /*
    http://www.palabras.com.ar/wp-json/wp/v2/posts?page=2
     */
    public void pedirListaDeLasUltimasNoticiasPublicadas(final ResultListener<List<Noticia>> escuchadorDelControlador, Integer pagina) {
        serviceNoticia = retrofit.create(ServiceNoticia.class);
        Call<List<Noticia>> llamada = serviceNoticia.pedirListaDeNoticias(pagina);

        llamada.enqueue(new Callback<List<Noticia>>() {
            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                escuchadorDelControlador.finish(response.body());
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {

            }
        });
    }

    public void pedirListaDeNotasPorCategoria(final ResultListener<List<Noticia>> escuchadorDelControlador, Integer pagina, Integer categoria) {
        serviceNoticia = retrofit.create(ServiceNoticia.class);

        Call<List<Noticia>> llamada = serviceNoticia.pedirListaDeNoticiasPorCategoria(categoria, pagina, 5);

        llamada.enqueue(new Callback<List<Noticia>>() {
            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                escuchadorDelControlador.finish(response.body());
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {

            }
        });
    }

    public void pedirListaDeNoticiasDeLaAgenda(Integer pagina, Integer tamaño, final ResultListener<List<Noticia>> escuchadorDelControlador) {
        serviceNoticia = retrofit.create(ServiceNoticia.class);
        Call<List<Noticia>> llamada = serviceNoticia.pedirListaDeNoticiasPorCategoria(34, pagina, tamaño);
        llamada.enqueue(new Callback<List<Noticia>>() {
            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                escuchadorDelControlador.finish(response.body());
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {

            }
        });
    }

    public void obtenerBusqueda(String busqueda, final ResultListener<List<Noticia>> escuchadorDelControlador) {
        serviceNoticia = retrofit.create(ServiceNoticia.class);
        Call<List<Noticia>> llamada = serviceNoticia.pedirListaDeNoticiasDeLaBusqueda(busqueda);

        llamada.enqueue(new Callback<List<Noticia>>() {
            @Override
            public void onResponse(Call<List<Noticia>> call, Response<List<Noticia>> response) {
                escuchadorDelControlador.finish(response.body());
            }

            @Override
            public void onFailure(Call<List<Noticia>> call, Throwable t) {

            }
        });
    }

    public void obtenerNotaPorID(Integer id, final ResultListener<Noticia> esuchadorDelControlador) {
        serviceNoticia = retrofit.create(ServiceNoticia.class);
        Call<Noticia> llamada = serviceNoticia.pedirUnaNoticiaPorID(id);

        llamada.enqueue(new Callback<Noticia>() {
            @Override
            public void onResponse(Call<Noticia> call, Response<Noticia> response) {
                esuchadorDelControlador.finish(response.body());
            }

            @Override
            public void onFailure(Call<Noticia> call, Throwable t) {

            }
        });
    }

}
