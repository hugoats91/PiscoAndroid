package com.pisco.app.ViewModel;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pisco.app.Enum.StateRecoverPassword;
import com.pisco.app.Enum.StateUser;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.LocalService.Entity.EntityStateOnboarding;
import com.pisco.app.LocalService.Entity.EntityUser;
import com.pisco.app.R;
import com.pisco.app.Repository.OAuthRepository;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.UtilDialog;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.Screen.Dialogs.ProgressDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LoginViewModel extends ViewModel {

    public interface LoginCallback{
        void onSuccess(long result, int userState);
        void onError(int type);
    }

    private OAuthRepository oauthRepository = new OAuthRepository();

    public LoginViewModel() {}

    public void logInOAuthUser(Fragment fragment, String name, String email, String password, String confirmPassword, String country, int type, int countryPortalId, Context context, LoginCallback callback) {
        ProgressDialogFragment dialog = UtilDialog.showProgress(fragment);
        ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().registerUserOAuth(name, email, password, confirmPassword, country, type, countryPortalId, new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                int result = response.body().getAsInt();
                if(result == 0){
                    logInEnterOAuthUser(email, password, type, context, callback);
                }else {
                    callback.onError(result);
                }

            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();
            }

        }, callback);
    }

    public void loginUser(Fragment fragment, String email, String password, boolean shouldKeepSession, Context context, LoginViewModel.LoginCallback callback) {
        LoginRegisterData data = new LoginRegisterData();
        data.setEmail(email);
        data.setPassword(password);
        data.setType(0);
        if (shouldKeepSession) {
            Query.saveValue(email, password, context);
        }
        ProgressDialogFragment dialog = UtilDialog.showProgress(fragment);
        Call<JsonElement> call = oauthRepository.postLogInSelectionEmailFront(data);
        call.enqueue(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                JsonElement jsonElement = response.body();
                if (jsonElement == null) {
                    return;
                }
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String token = jsonObject.get("token").getAsString();
                    int userType = jsonObject.get("TipoUsuario").getAsInt();
                    int consultationResponse = jsonObject.get("RespuestaConsulta").getAsInt();
                    String userName = jsonObject.get("NombreUsuario").getAsString();
                    int userState = jsonObject.get("EstadoUsuario").getAsInt();
                    int welcomeState = jsonObject.get("EstadoBienvenido").getAsInt();
                    int initialScore = jsonObject.get("puntageInicio").getAsInt();
                    int baseScore = jsonObject.get("puntageBase").getAsInt();
                    int currentScore = jsonObject.get("puntageAcumulado").getAsInt();
                    String learnPisco = jsonObject.get("aprendePisco").getAsString();
                    int rouletteNumber = jsonObject.get("nroRuleta").getAsInt();
                    String rouletteImage = jsonObject.get("imagenRuleta").getAsString();
                    String imagePath = jsonObject.get("rutaImagen").getAsString();
                    int portalId = jsonObject.get("PortalId").getAsInt();
                    JsonArray jsonArrayStateListOnboarding = jsonObject.getAsJsonArray("listaEstadoOnnboarding");
                    if (consultationResponse == StateUser.EXISTS.ordinal()) {
                        AppDatabase.INSTANCE.userDao().deleteAll();
                        AppDatabase.INSTANCE.userDao().deleteAllEntityStateOnboarding();
                        EntityUser entityUser = new EntityUser(email, token, userType, consultationResponse, userName, userState, welcomeState, initialScore, baseScore, currentScore, learnPisco, rouletteNumber, rouletteImage, imagePath, portalId);
                        for (int i = 0; i < jsonArrayStateListOnboarding.size(); i++) {
                            if (i == jsonArrayStateListOnboarding.size()) break;
                            int sessionState = jsonArrayStateListOnboarding.get(i).getAsJsonObject().get("SesiEstado").getAsInt();
                            int pageId = jsonArrayStateListOnboarding.get(i).getAsJsonObject().get("PagiId").getAsInt();
                            String pageName = jsonArrayStateListOnboarding.get(i).getAsJsonObject().get("PagiNombre").getAsString();
                            EntityStateOnboarding entityStateOnboarding = new EntityStateOnboarding(sessionState, pageId, pageName);
                            AppDatabase.INSTANCE.userDao().insertStateOnboarding(entityStateOnboarding);
                        }
                        long result = AppDatabase.INSTANCE.userDao().insert(entityUser);
                        Query.saveLoginType(context, 0);
                        callback.onSuccess(result, userState);
                    } else{
                        callback.onError(consultationResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();
            }

        });
    }

    public void logInEnterOAuthUser(String email, String password, int type, Context context, LoginViewModel.LoginCallback callback) {
        LoginRegisterData data = new LoginRegisterData();
        data.setEmail(email);
        data.setPassword(password);
        data.setType(type);
        Query.saveValue(email, password, context);
        Call<JsonElement> call = oauthRepository.postLogInSelectionEmailFront(data);
        call.enqueue(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                if (jsonElement == null) {
                    return;
                }
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String token = jsonObject.get("token").getAsString();
                    int userType = jsonObject.get("TipoUsuario").getAsInt();
                    int consultationResponse = jsonObject.get("RespuestaConsulta").getAsInt();
                    String userName = jsonObject.get("NombreUsuario").getAsString();
                    int userState = jsonObject.get("EstadoUsuario").getAsInt();
                    int welcomeState = jsonObject.get("EstadoBienvenido").getAsInt();
                    int initialScore = jsonObject.get("puntageInicio").getAsInt();
                    int baseScore = jsonObject.get("puntageBase").getAsInt();
                    int currentScore = jsonObject.get("puntageAcumulado").getAsInt();
                    String learnPisco = jsonObject.get("aprendePisco").getAsString();
                    int rouletteNumber = jsonObject.get("nroRuleta").getAsInt();
                    String rouletteImage = jsonObject.get("imagenRuleta").getAsString();
                    String imagePath = jsonObject.get("rutaImagen").getAsString();
                    int portalId = jsonObject.get("PortalId").getAsInt();
                    JsonArray jsonArrayStateListOnboarding = jsonObject.getAsJsonArray("listaEstadoOnnboarding");
                    if (consultationResponse == StateUser.EXISTS.ordinal()) {
                        AppDatabase.INSTANCE.userDao().deleteAll();
                        AppDatabase.INSTANCE.userDao().deleteAllEntityStateOnboarding();
                        EntityUser entityUser = new EntityUser(email, token, userType, consultationResponse, userName, userState, welcomeState, initialScore, baseScore, currentScore, learnPisco, rouletteNumber, rouletteImage, imagePath, portalId);
                        for (int i = 0; i < jsonArrayStateListOnboarding.size(); i++) {
                            if (i == jsonArrayStateListOnboarding.size()) break;
                            int sessionState = jsonArrayStateListOnboarding.get(i).getAsJsonObject().get("SesiEstado").getAsInt();
                            int pageId = jsonArrayStateListOnboarding.get(i).getAsJsonObject().get("PagiId").getAsInt();
                            String pageName = jsonArrayStateListOnboarding.get(i).getAsJsonObject().get("PagiNombre").getAsString();
                            EntityStateOnboarding entityStateOnboarding = new EntityStateOnboarding(sessionState, pageId, pageName);
                            AppDatabase.INSTANCE.userDao().insertStateOnboarding(entityStateOnboarding);
                        }
                        long result = AppDatabase.INSTANCE.userDao().insert(entityUser);
                        Query.saveLoginType(context, type);
                        callback.onSuccess(result, userState);
                    }else{
                        callback.onError(consultationResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {}

        });
    }

    public void postRecoverPasswordFront(Fragment fragment, String email) {
        LoginRegisterData data = new LoginRegisterData();
        data.setEmail(email);
        ProgressDialogFragment dialog = UtilDialog.showProgress(fragment);
        Call<JsonElement> call = oauthRepository.postRecoverPasswordFront(data);
        call.enqueue(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                dialog.dismiss();
                JsonElement jsonElement = response.body();
                if (jsonElement == null) {
                    return;
                }
                try {
                    int responseCode = jsonElement.getAsInt();
                    View view = ViewInstanceList.getDictionaryViews("login-email-password-recuperar-fragment");
                    switch (responseCode){
                        case 1:
                            Navigation.findNavController(view).navigate(R.id.action_loginEmailPasswordRecuperarFragment_to_loginEmailPasswordRecuperarExitosoFragment);
                            break;

                        case 3:
                            if (Query.getPortalId() == 1) {
                                UtilDialog.infoMessage(fragment.getContext(), fragment.getString(R.string.app_name), fragment.getString(R.string.app_en_recuperar_no_existe));
                            }else{
                                UtilDialog.infoMessage(fragment.getContext(), fragment.getString(R.string.app_name), fragment.getString(R.string.app_es_recuperar_no_existe));
                            }
                            break;

                        case 4:
                            if (Query.getPortalId() == 1) {
                                UtilDialog.infoMessage(fragment.getContext(), fragment.getString(R.string.app_name), fragment.getString(R.string.app_en_recuperar_no_medio));
                            }else{
                                UtilDialog.infoMessage(fragment.getContext(), fragment.getString(R.string.app_name), fragment.getString(R.string.app_es_recuperar_no_medio));
                            }
                            break;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                dialog.dismiss();
            }

        });
    }

}