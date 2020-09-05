package com.pisco.app.Repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Entity.RecipeItem;
import com.pisco.app.Utils.RetrofitService;
import com.pisco.app.ViewModel.LiveData.Drink;
import com.pisco.app.ViewModel.LiveData.CityData;
import com.pisco.app.ViewModel.LiveData.CitySaleData;
import com.pisco.app.ViewModel.LiveData.StateOnboardingData;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.ViewModel.LiveData.Question;
import com.pisco.app.ViewModel.LiveData.RecipeData;
import com.pisco.app.WebService.WebAuthorizationService;
import com.pisco.app.Remote.UpdateAvatarRemote;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class OAuthAuthorizationRepository implements WebAuthorizationService {

    private WebAuthorizationService webservice;

    public OAuthAuthorizationRepository(){
        webservice = RetrofitService.createServiceAuthorization(WebAuthorizationService.class);
    }

    @Override
    public Call<JsonElement> postUpdateWelcomeFront() {
        return webservice.postUpdateWelcomeFront();
    }

    @Override
    public Call<JsonElement> postUpdateOnboardingFront(StateOnboardingData estadoOnnboardingData) {
        return webservice.postUpdateOnboardingFront(estadoOnnboardingData);
    }
    @Override
    public Call<JsonElement> postUpdateOnboardingSkipFront(StateOnboardingData estadoOnnboardingData) {
        return webservice.postUpdateOnboardingFront(estadoOnnboardingData);
    }
    @Override
    public Call<JsonElement> postUpdateOnboardingReadyFront(StateOnboardingData estadoOnnboardingData) {
        return webservice.postUpdateOnboardingReadyFront(estadoOnnboardingData);
    }
    @Override
    public Call<List<JsonElement>> postDrinkListFront() {
        return webservice.postDrinkListFront();
    }

    @Override
    public Call<List<JsonElement>> postPromotionListFront() {
        return webservice.postPromotionListFront();
    }

    @Override
    public Call<ArrayList<JsonObject>> postQuestionListFront(Drink bebida) {
        return webservice.postQuestionListFront(bebida);
    }
    @Override
    public Call<JsonElement> postInsertUserAnswerFront(Question pregunta) {
        return webservice.postInsertUserAnswerFront(pregunta);
    }

    @Override
    public Call<JsonElement> postUserPointListFront() {
        return webservice.postUserPointListFront();
    }

    @Override
    public Call<ArrayList<JsonObject>> postCityListFront() {
        return webservice.postCityListFront();
    }

    @Override
    public Call<ArrayList<JsonObject>> postPointListCitySalesFront(CityData ciudadData) {
        return webservice.postPointListCitySalesFront(ciudadData);
    }

    @Override
    public Call<ArrayList<JsonObject>> postStoreListOnlineFront(CitySaleData ciudadVentaData) {
        return webservice.postStoreListOnlineFront(ciudadVentaData);
    }
    @Override
    public Call<ArrayList<JsonObject>> postSalePointDetailFront(CitySaleData ciudadVentaData) {
        return webservice.postSalePointDetailFront(ciudadVentaData);
    }

    @Override
    public Call<ArrayList<JsonObject>> postRecipeListFront(RecipeData recetaData) {
        return webservice.postRecipeListFront(recetaData);
    }
    @Override
    public Call<ArrayList<JsonObject>> postActiveRecipeListFront() {
        return webservice.postActiveRecipeListFront();
    }
    @Override
    public Call<ArrayList<JsonObject>> postBlockedRecipeListFront() {
        return webservice.postBlockedRecipeListFront();
    }
    @Override
    public Call<JsonElement> postUpdateLikeFront(RecipeItem recipeItem) {
        return webservice.postUpdateLikeFront(recipeItem);
    }
    @Override
    public Call<JsonElement> postUserDataFront() {
        return webservice.postUserDataFront();
    }

    @Override
    public Call<JsonElement> postUpdateUserFront(LoginRegisterData loginRegisterData) {
        return webservice.postUpdateUserFront(loginRegisterData);
    }

    @Override
    public Call<JsonElement> postUpdateAvatar(UpdateAvatarRemote updateAvatarRemote) {
        return webservice.postUpdateAvatar(updateAvatarRemote);
    }

    @Override
    public Call<JsonElement> postLogInSelectionEmailFront(LoginRegisterData loginRegisterData) {
        return webservice.postLogInSelectionEmailFront(loginRegisterData);
    }

}