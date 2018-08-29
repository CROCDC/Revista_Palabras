package com.example.tcr.revistapalabras.Model;


import java.io.Serializable;

public class Imagen implements Serializable {
    private Integer id;
    private String source_url;
    private Media_details media_details;

    public Integer getId() {
        return id;
    }

    public String getSource_url() {
        return source_url;
    }

    public Media_details getMedia_details() {
        return media_details;
    }
}
