package com.ar.tcr.revistapalabras.Model;

import java.io.Serializable;

/**
 * Created by TCR on 05/06/2018.
 */

public class Content implements Serializable {
    private String rendered;//descripcion

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }
}
