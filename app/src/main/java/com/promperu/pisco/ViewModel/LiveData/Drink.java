package com.promperu.pisco.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class Drink {

    @SerializedName("BebiId")
    int drinkId;

    public Drink(int drinkId) {
        this.drinkId = drinkId;
    }

}