package com.pisco.app.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class RecipeData {

    @SerializedName("ReceId")
    int recipeId;

    public RecipeData(int recipeId) {
        this.recipeId = recipeId;
    }

}