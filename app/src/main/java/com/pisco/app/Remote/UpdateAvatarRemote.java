package com.pisco.app.Remote;

import com.google.gson.annotations.SerializedName;

public class UpdateAvatarRemote {

    @SerializedName("Imagen")
    private String image;

    public UpdateAvatarRemote(String image) {
        this.image = image;
    }

}