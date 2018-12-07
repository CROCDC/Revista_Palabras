package com.ar.tcr.revistapalabras.Model;

public class Publicidad extends RecyclerViewItem {
    private String url;
    private String link;

    public Publicidad() {
    }

    public Publicidad(String url, String link) {
        this.url = url;
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
