package com.example.tcr.revistapalabras.Model;

import java.io.Serializable;

public class Sizes implements Serializable {
    private Thumbnail thumbnail;
    private Medium medium;
    private MediumLarge medium_large;
    private Large large;
    private Full full;

    public Medium getMedium() {
        return medium;
    }


    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public MediumLarge getMedium_large() {
        return medium_large;
    }

    public Large getLarge() {
        return large;
    }

    public Full getFull() {
        return full;
    }
}
