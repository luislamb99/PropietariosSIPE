package com.biotecnum.propietariossipe;

/**
 * Created by ingluismb on 31/10/17.
 */

public class InfoPropietarios {

    public String password;

    public InfoPropietarios() {
        // Default constructor required for calls to DataSnapshot.getValue(infoConductor.class)
    }

    public InfoPropietarios(String password) {

        this.password = password;

    }
}
