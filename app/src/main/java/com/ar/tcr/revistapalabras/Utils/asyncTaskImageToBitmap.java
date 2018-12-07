package com.ar.tcr.revistapalabras.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ar.tcr.revistapalabras.Model.ImageBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class asyncTaskImageToBitmap extends AsyncTask<ImageBitmap, Void, Bitmap> {

    ImageBitmap imageBitmap = null;

    @Override
    public Bitmap doInBackground(ImageBitmap... imageBitmaps) {
        this.imageBitmap = imageBitmaps[0];
        return download_Image(imageBitmaps[0].getUrl());
    }

    @Override
    public void onPostExecute(Bitmap result) {
        imageBitmap.setBitmap(result);

    }


    private Bitmap download_Image(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



}
