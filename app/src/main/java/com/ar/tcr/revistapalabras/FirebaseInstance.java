package com.ar.tcr.revistapalabras;

import com.ar.tcr.revistapalabras.DAO.DAOApiPushNotification;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class FirebaseInstance extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        new DAOApiPushNotification().insertarToken();
    }



}
