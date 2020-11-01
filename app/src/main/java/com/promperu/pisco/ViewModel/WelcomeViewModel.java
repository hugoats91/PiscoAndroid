package com.promperu.pisco.ViewModel;

import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.Repository.OAuthAuthorizationRepository;
import com.promperu.pisco.ViewModel.LiveData.StateOnboardingData;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class WelcomeViewModel extends ViewModel {

    private OAuthAuthorizationRepository oauthRepository = new OAuthAuthorizationRepository();

    public WelcomeViewModel() {}

    public interface UpdateWelcomeCallback{
        void onSuccess();
    }

    public void updateWelcomeFront(WelcomeViewModel.UpdateWelcomeCallback callback) {
        int pagiId = AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getPageId();
        StateOnboardingData stateOnboardingData = new StateOnboardingData(pagiId);
        Call<JsonElement> call = oauthRepository.postUpdateOnboardingFront(stateOnboardingData);
        call.enqueue(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    AppDatabase.INSTANCE.userDao().setUpdateEntityStateOnboarding(0,"Bienvenidos");
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonElement> call, @NotNull Throwable t) {}

        });
    }

    public void updateReadyOnboardingFront(String onboardAdapter, String pageNumber, WelcomeViewModel.UpdateWelcomeCallback callback){
        int pagiId = AppDatabase.INSTANCE.userDao().getEntityStateOnboarding(pageNumber).getPageId();
        StateOnboardingData stateOnboardingData = new StateOnboardingData(pagiId);
        Call<JsonElement> call = oauthRepository.postUpdateOnboardingSkipFront(stateOnboardingData);
        call.enqueue(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    AppDatabase.INSTANCE.userDao().setUpdateEntityStateOnboarding(0, onboardAdapter);
                    int sesiEstado = AppDatabase.INSTANCE.userDao().getEntityStateOnboarding(pageNumber).getSesiState();
                    if (sesiEstado != 1) {
                        callback.onSuccess();
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) { callback.onSuccess();}

        });
    }

}