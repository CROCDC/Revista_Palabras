package com.ar.tcr.revistapalabras.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mySingleton;
    private static Context context;
    private RequestQueue requestQueue;

    private MySingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        //IF INDENTEADO
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        return requestQueue;

    }

    public static synchronized MySingleton getmInstance(Context context){
        if (mySingleton == null)
            mySingleton = new MySingleton((context));
        return mySingleton;
    }

    public <T> void addToRequestQue(Request<T> request){
        getRequestQueue().add(request);
    }
}
