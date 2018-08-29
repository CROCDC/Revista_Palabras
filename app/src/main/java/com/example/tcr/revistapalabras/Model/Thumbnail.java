package com.example.tcr.revistapalabras.Model;

import java.io.Serializable;

public class Thumbnail implements Serializable {

    private String source_url;

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getSource_url() {
        return source_url;

    }
}
