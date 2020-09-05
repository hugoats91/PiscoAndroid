package com.pisco.app.Entity;

import com.google.gson.annotations.SerializedName;

public class RecipeItem {

    private String title, preparation, description, image, imageHeader;
    private int userId;

    @SerializedName("ReceId")
    private int recipeId;
    @SerializedName("ReceUsuarioMeGusta")
    private int userLike;

    public RecipeItem(String title, String preparation, String description, String image, String imageHeader, int recipeId, int userLike, int userId) {
        this.title = title;
        this.preparation = preparation;
        this.description = description;
        this.image = image;
        this.imageHeader = imageHeader;
        this.recipeId = recipeId;
        this.userLike = userLike;
        this.userId = userId;
    }

    public RecipeItem(int userLike, int recipeId){
        this.userLike = userLike;
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public String getPreparation() {
        return preparation;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserLike() {
        return userLike;
    }

    public int getUserId() {
        return userId;
    }

}