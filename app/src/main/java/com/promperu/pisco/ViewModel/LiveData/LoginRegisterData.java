package com.promperu.pisco.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class LoginRegisterData {

    @SerializedName("UsuaNombre")
    public String name = null;

    @SerializedName("UsuaApellidos")
    public String lastName = null;

    @SerializedName("UsuaCorreo")
    public String email = null;

    @SerializedName("UsuaContrasena")
    public String password = null;

    @SerializedName("PaisCodigoDos")
    public String countryCodeTwo = null;

    @SerializedName("PaisId")
    public int countryId = 0;

    @SerializedName("PaisPortalId")
    public int countryPortalId = 0;

    @SerializedName("UsuaTipoUsuario")
    public int type = 0;

    @SerializedName("UsuaImagen")
    public String image = null;

    public LoginRegisterData(){ }

    public LoginRegisterData(int countryId, String name, String lastName, String email, String password, int countryPortalId, String image) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.countryPortalId = countryPortalId;
        this.countryId = countryId;
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCountryCodeTwo(String countryCodeTwo) {
        this.countryCodeTwo = countryCodeTwo;
    }

    public void setCountryPortalId(int countryPortalId) {
        this.countryPortalId = countryPortalId;
    }

}