package com.ar.tcr.revistapalabras.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImageBitmap implements Serializable {
    private String url;
    private Bitmap bitmap;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
