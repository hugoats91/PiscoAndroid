package com.pisco.app.WebService.Entity;

import com.google.gson.annotations.SerializedName;

public class EntityUser {

    @SerializedName("correo")
    private String email;

    @SerializedName("password")
    private String password;

    public EntityUser(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}