package com.example.tcr.revistapalabras.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TCR on 05/06/2018.
 */

public class Links implements Serializable {


    @SerializedName("wp:featuredmedia")
    @Expose
    private List<wpfeaturedmedia> wpfeaturedmediaList;

}
