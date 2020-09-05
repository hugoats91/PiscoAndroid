package com.pisco.app.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class StateOnboardingData {

    @SerializedName("PagiId")
    public int pageId;

    public StateOnboardingData(int pageId) {
        this.pageId = pageId;
    }

}