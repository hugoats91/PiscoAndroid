package com.pisco.app.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class CityData {

    @SerializedName("PuntVentaCiudadId")
    int cityPointSaleId;

    public CityData(int cityPointSaleId) {
        this.cityPointSaleId = cityPointSaleId;
    }

}