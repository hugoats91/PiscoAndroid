package com.pisco.app.ViewModel;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Entity.RecipeItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.LocalService.Entity.EntityUser;
import com.pisco.app.R;
import com.pisco.app.Repository.OAuthAuthorizationRepository;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.LiveData.Drink;
import com.pisco.app.ViewModel.LiveData.CityData;
import com.pisco.app.ViewModel.LiveData.CitySaleData;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.ViewModel.LiveData.Question;
import com.pisco.app.ViewModel.LiveData.RecipeData;
import com.pisco.app.Remote.UpdateAvatarRemote;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class HomeViewModel extends ViewModel {

    private OAuthAuthorizationRepository oauthRepository = new OAuthAuthorizationRepository();
    private ArrayList<JsonObject> cityArrayList;
    private List<String> cityList = new ArrayList<>();

    public List<Integer> cityIdList = new ArrayList<>();

    public HomeViewModel() {}

    public void roulette(Callback<List<JsonElement>> calling) {
        Call<List<JsonElement>> call = oauthRepository.postDrinkListFront();
        call.enqueue(calling);
    }

    public void promotionListPisco(Callback<List<JsonElement>> callback) {
        Call<List<JsonElement>> call = oauthRepository.postPromotionListFront();
        call.enqueue(callback);
    }

    public void answerQuestionUser(Question question, Callback<JsonElement> callback) {
        Call<JsonElement> call = oauthRepository.postInsertUserAnswerFront(question);
        call.enqueue(callback);
    }

    public void questionList(Callback<ArrayList<JsonObject>> calling) {
        EntityUser user = AppDatabase.INSTANCE.userDao().getEntityUser();
        int bebiId = user.getBebiId();
        if (bebiId != 0) {
            Drink drink = new Drink(bebiId);
            //AppDatabase.INSTANCE.userDao().setUpdateBebiId(0);
            Call<ArrayList<JsonObject>> call = oauthRepository.postQuestionListFront(drink);
            call.enqueue(calling);
        }
    }

    public void currentScore(Callback<JsonElement> calling) {
        Call<JsonElement> call = oauthRepository.postUserPointListFront();
        call.enqueue(calling);
    }

    public void postUpdateLikeFront(RecipeItem recipeItem, Callback<JsonElement> calling) {
        Call<JsonElement> call = oauthRepository.postUpdateLikeFront(recipeItem);
        call.enqueue(calling);
    }

    public void addCitySpinner(Spinner spinner) {
        View view = ViewInstanceList.getDictionaryViews("in-donde-comprar-fragment");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.item_spinner, cityList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void postGetCityListFront(Callback<ArrayList<JsonObject>> calling) {
        Call<ArrayList<JsonObject>> call = oauthRepository.postCityListFront();
        call.enqueue(calling);
    }

    public void cityListFront(Spinner spinnerCity) {
        Call<ArrayList<JsonObject>> call = oauthRepository.postCityListFront();
        call.enqueue(new Callback<ArrayList<JsonObject>>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                cityArrayList = response.body();
                cityList = new ArrayList<>();
                if (cityArrayList != null) {
                    for (int i = 0; i < cityArrayList.size(); i++) {
                        String pointSaleCityName = cityArrayList.get(i).getAsJsonObject().get("PuntVentaCiudadNombre").toString().replaceAll("\"", "");
                        int cityPointId = cityArrayList.get(i).getAsJsonObject().get("PuntoCiudadId").getAsInt();
                        cityList.add(pointSaleCityName);
                        cityIdList.add(cityPointId);
                    }
                }
                addCitySpinner(spinnerCity);
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {}

        });
    }

    public void citySaleList(CityData cityData, Callback<ArrayList<JsonObject>> callback) {
        Call<ArrayList<JsonObject>> call = oauthRepository.postPointListCitySalesFront(cityData);
        call.enqueue(callback);
    }

    public void postPointSaleDetailFront(CitySaleData citySaleData, Callback<ArrayList<JsonObject>> callback) {
        Call<ArrayList<JsonObject>> call = oauthRepository.postSalePointDetailFront(citySaleData);
        call.enqueue(callback);
    }

    public void getSaleStoreListOnline(CitySaleData citySaleData, Callback<ArrayList<JsonObject>> callback) {
        Call<ArrayList<JsonObject>> call = oauthRepository.postStoreListOnlineFront(citySaleData);
        call.enqueue(callback);
    }

    public void postRecipeListFront(RecipeData recipeData, Callback<ArrayList<JsonObject>> calling){
        Call<ArrayList<JsonObject>> call = oauthRepository.postRecipeListFront(recipeData);
        call.enqueue(calling);
    }

    public void postActiveRecipeListFront(Callback<ArrayList<JsonObject>> calling){
        Call<ArrayList<JsonObject>> call = oauthRepository.postActiveRecipeListFront();
        call.enqueue(calling);
    }

    public void postBlockedRecipeListFront(Callback<ArrayList<JsonObject>> calling){
        Call<ArrayList<JsonObject>> call = oauthRepository.postBlockedRecipeListFront();
        call.enqueue(calling);
    }

    public void postUserDataFront(Callback<JsonElement> calling) {
        Call<JsonElement> call = oauthRepository.postUserDataFront();
        call.enqueue(calling);
    }

    public void postUpdateUserFront(Callback<JsonElement> calling, LoginRegisterData data) {
        data.setCountryPortalId(AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId());
        Call<JsonElement> call = oauthRepository.postUpdateUserFront(data);
        call.enqueue(calling);
    }

    public void postEmailSelectionFront(LoginRegisterData loginRegisterData, Callback<JsonElement> calling){
        Call<JsonElement> call = oauthRepository.postLogInSelectionEmailFront(loginRegisterData);
        call.enqueue(calling);
    }

    public void postUpdateAvatarFront(Callback<JsonElement> calling, UpdateAvatarRemote data) {
        Call<JsonElement> call = oauthRepository.postUpdateAvatar(data);
        call.enqueue(calling);
    }

}