package com.pisco.app.WebService;

import com.google.gson.JsonElement;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.ViewModel.LiveData.CountryData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebService {

    @Headers("Content-Type: application/json")
    @POST("Front/PostRegistarUsuarioFront")
    Call<JsonElement> postRegisterUserFront(@Body LoginRegisterData loginRegisterData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostListarPaisFront")
    Call<JsonElement> postCountryListFront(@Body CountryData paisData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostSeleccionarLogueoCorreoFront")
    Call<JsonElement> postLogInSelectionEmailFront(@Body LoginRegisterData loginRegisterData);

    @Headers("Content-Type: application/json")
    @POST("Front/PostSeleccionarContrasenaFront")
    Call<JsonElement> postRecoverPasswordFront(@Body LoginRegisterData loginRegisterData);

}