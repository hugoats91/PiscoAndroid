package com.pisco.app.Entity;

import org.jetbrains.annotations.NotNull;

public class Country {
    private String paisNombre;
    private String paisCodigoDos;
    private int paisPortalId;
    private int paisId;

    public String getPaisNombre() {
        return paisNombre;
    }

    public void setPaisNombre(String paisNombre) {
        this.paisNombre = paisNombre;
    }

    public String getPaisCodigoDos() {
        return paisCodigoDos;
    }

    public void setPaisCodigoDos(String paisCodigoDos) {
        this.paisCodigoDos = paisCodigoDos;
    }

    public int getPaisPortalId() {
        return paisPortalId;
    }

    public void setPaisPortalId(int paisPortalId) {
        this.paisPortalId = paisPortalId;
    }

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

    public Country(String paisNombre, String paisCodigoDos, int paisPortalId, int paisId) {
        this.paisNombre = paisNombre;
        this.paisCodigoDos = paisCodigoDos;
        this.paisPortalId = paisPortalId;
        this.paisId = paisId;
    }

    @NotNull
    @Override
    public String toString() {
        return paisNombre;
    }
}
