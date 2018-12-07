package com.ar.tcr.revistapalabras.DAO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ar.tcr.revistapalabras.Model.Imagen;
import com.ar.tcr.revistapalabras.Utils.Helper;
import com.ar.tcr.revistapalabras.Utils.ResultListener;
import com.ar.tcr.revistapalabras.Utils.ServiceImagenes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DAOImagenesRetrofit {
    private Retrofit retrofit;
    private ServiceImagenes serviceImagenes;

    public DAOImagenesRetrofit(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.urlBase)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.client(httpClient.build()).build();
    }

    public void pedirDeImagenPorid(Integer idNota, final ResultListener<Imagen> escuchadorDelControlador){
        serviceImagenes = retrofit.create(ServiceImagenes.class);
        Call<Imagen> llamada = serviceImagenes.pedirImagenPorID(idNota);

        llamada.enqueue(new Callback<Imagen>() {
            @Override
            public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                escuchadorDelControlador.finish(response.body());
            }

            @Override
            public void onFailure(Call<Imagen> call, Throwable t) {

            }
        });
    }
    public void imagenToBitmap (String src, final ResultListener<Bitmap> escuchadorDelControlador){
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            escuchadorDelControlador.finish(myBitmap);
        } catch (IOException e) {
            // Log exception
        }
    }
}
