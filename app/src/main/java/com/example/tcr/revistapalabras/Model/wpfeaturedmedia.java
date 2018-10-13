package com.example.tcr.revistapalabras.Model;

import java.io.Serializable;

/**
 * Created by TCR on 05/06/2018.
 */

public class wpfeaturedmedia implements Serializable {
    private String href;//pedido de imagenes
    private String source_url;//pedido de imagenes
    private Media_details media_details;

    public String getHref() {
        return href;
    }

    public Media_details getMedia_details() {
        return media_details;
    }

    public String getSource_url() {
        return source_url;
    }
}
