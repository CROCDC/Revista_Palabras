package com.example.tcr.revistapalabras;
import android.util.Log;


import com.example.tcr.revistapalabras.DAO.DAOApiPushNotification;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class FirebaseInstance extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        new DAOApiPushNotification().insertarToken();
    }



}
