package com.pisco.app.Repository;

import com.google.gson.JsonElement;
import com.pisco.app.Utils.RetrofitService;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.ViewModel.LiveData.CountryData;
import com.pisco.app.WebService.WebService;

import retrofit2.Call;

public class OAuthRepository implements WebService {

    private WebService webservice;

    public OAuthRepository(){
        webservice = RetrofitService.createService(WebService.class);
    }

    @Override
    public Call<JsonElement> postRegisterUserFront(LoginRegisterData loginRegisterData) {
        return webservice.postRegisterUserFront(loginRegisterData);
    }

    @Override
    public Call<JsonElement> postCountryListFront(CountryData paisData) {
        return webservice.postCountryListFront(paisData);
    }

    @Override
    public Call<JsonElement> postLogInSelectionEmailFront(LoginRegisterData loginRegisterData) {
        return webservice.postLogInSelectionEmailFront(loginRegisterData);
    }

    @Override
    public Call<JsonElement> postRecoverPasswordFront(LoginRegisterData loginRegisterData) {
        return webservice.postRecoverPasswordFront(loginRegisterData);
    }

}