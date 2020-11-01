package com.promperu.pisco.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class CountryData {

    @SerializedName("PaisPortalId")
    public int countryPortalId;

    public CountryData(int countryPortalId) {
        this.countryPortalId = countryPortalId;
    }

}