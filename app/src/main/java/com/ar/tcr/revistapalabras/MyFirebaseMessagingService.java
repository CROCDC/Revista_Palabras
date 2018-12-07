package com.ar.tcr.revistapalabras;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.ar.tcr.revistapalabras.Controler.ControlerNoticias;
import com.ar.tcr.revistapalabras.Model.Noticia;
import com.ar.tcr.revistapalabras.Utils.Helper;
import com.ar.tcr.revistapalabras.Utils.MySingleton;
import com.ar.tcr.revistapalabras.Utils.ResultListener;
import com.ar.tcr.revistapalabras.View.Activitys.DescripcionesDeNotasActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String CLAVE_ID = "id";
    private static final String CLAVE_TITULO = "title";
    private static final String CLAVE_GRUPO_NOTIFICACIONES = "grupo";

    private NotificationManager notificationManager;

    private String url;
    private String titulo;
    private String descripcion;

    private ImageRequest imageRequest;


    private String id = "";
    private String ADMIN_CHANNEL_ID = "random 1 1";

    private Intent notificationIntent;
    private Bundle bundle;
    private PendingIntent pendingIntent;



    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            id = remoteMessage.getData().get(CLAVE_ID);
            titulo = remoteMessage.getData().get(CLAVE_TITULO);

        }

        if (id.equals("0")) {
            //CODIGO PARA VERIFICAR SI FUNCIONAN LAS NOTIFICACIONES
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels();
            }
            final int notificationId = new Random().nextInt(60000);

            notificationIntent = new Intent(this, DescripcionesDeNotasActivity.class);

            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

            url = "http://www.palabras.com.ar/wp-content/uploads/2018/08/Buenos-Aires-GIN-2-1152x759.jpg";

            imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), ADMIN_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_stat_faviconretina)
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentText(remoteMessage.getData().get("message"))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setStyle(new NotificationCompat.BigPictureStyle()
                                    .bigPicture(response));


                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(notificationId, notificationBuilder.build());


                }
            }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MySingleton.getmInstance(getApplicationContext()).addToRequestQue(imageRequest);


        } else {

            new ControlerNoticias().pedirNoticiasPorID(Integer.parseInt(id), new ResultListener<Noticia>() {
                @Override
                public void finish(final Noticia noticia) {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        setupChannels();
                    }
                    final int notificationId = new Random().nextInt(60000);

                    notificationIntent = new Intent(MyFirebaseMessagingService.this, DescripcionesDeNotasActivity.class);

                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    bundle = new Bundle();

                    bundle.putSerializable(DescripcionesDeNotasActivity.CLAVE_OBJETO_CONTENIDO, noticia);

                    notificationIntent.putExtras(bundle);

                    pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

                    try {
                        url = noticia.getEmbedded().getListaDeImagenes().get(0).getMedia_details().getSizes().getMedium_large().getSource_url();

                    }catch (Exception e){
                        url = noticia.getEmbedded().getListaDeImagenes().get(0).getHref();
                    }
                    titulo = noticia.getTitle().getRendered();
                    descripcion = noticia.getExcerpt().getRendered();

                    titulo = Helper.eliminarEtiquetasHTML(titulo);
                    descripcion = Helper.eliminarEtiquetasHTML(descripcion);

                    imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {

                            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), ADMIN_CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_stat_faviconretina)
                                    .setContentTitle(titulo)
                                    .setContentText(descripcion)
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(pendingIntent)
                                    .setStyle(new NotificationCompat.BigPictureStyle()
                                            .bigPicture(response));


                            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(notificationId, notificationBuilder.build());


                        }
                    }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    MySingleton.getmInstance(getApplicationContext()).addToRequestQue(imageRequest);


                }


            });

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = "Revista Palabras";
        String adminChannelDescription = "Palabras";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}


