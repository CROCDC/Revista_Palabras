package com.ar.tcr.revistapalabras.DAO;

import com.ar.tcr.revistapalabras.Utils.Helper;
import com.ar.tcr.revistapalabras.Utils.ServiceToken;
import com.google.firebase.iid.FirebaseInstanceId;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DAOApiPushNotification {
    private Retrofit retrofit;
    private ServiceToken serviceToken;


    public DAOApiPushNotification() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Helper.urlAPINotification)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.client(httpClient.build()).build();


    }


    public void insertarToken() {
        serviceToken = retrofit.create(ServiceToken.class);

        final RequestBody requestBodyToken = RequestBody.create(MediaType.parse("text/plain"), FirebaseInstanceId.getInstance().getToken());
        final RequestBody requestSistema = RequestBody.create(MediaType.parse("text/plain"), "Android");


        serviceToken.agregarToken(requestBodyToken, requestSistema).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
