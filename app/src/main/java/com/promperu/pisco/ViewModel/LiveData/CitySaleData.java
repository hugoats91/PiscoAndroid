package com.promperu.pisco.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class CitySaleData {

    @SerializedName("PuntId")
    int PuntId;

    @SerializedName("PuntVentaCiudadId")
    int PuntVentaCiudadId;

    public CitySaleData(int puntId, int PuntVentaCiudadId) {
        this.PuntId = puntId;
        this.PuntVentaCiudadId = PuntVentaCiudadId;
    }

}