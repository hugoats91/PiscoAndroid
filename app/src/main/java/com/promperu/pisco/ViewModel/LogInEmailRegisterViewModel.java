package com.promperu.pisco.ViewModel;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;
import com.promperu.pisco.Entity.Country;
import com.promperu.pisco.Enum.StateSignUpUserNoAuth;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.R;
import com.promperu.pisco.Repository.OAuthRepository;
import com.promperu.pisco.Utils.Query;
import com.promperu.pisco.Utils.UtilUser;
import com.promperu.pisco.Utils.ViewInstanceList;
import com.promperu.pisco.ViewModel.LiveData.CountryData;
import com.promperu.pisco.ViewModel.LiveData.LoginRegisterData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LogInEmailRegisterViewModel extends ViewModel {

    private OAuthRepository oauthRepository = new OAuthRepository();
    private String[] arrayCountry;

    public interface RegisterCallback {
        void onSuccess();

        void onError(int type);
    }

    public boolean validatePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public void registerUser(String name, String email, String password, String confirmPassword, String country, int type, int countryPortalId, JsonElement countryJsonElement, RegisterCallback callback) {
        LoginRegisterData data = new LoginRegisterData();
        if (validatePassword(password, confirmPassword)) {
            data.setName(name);
            data.setCountryCodeTwo(countryCodeTwo(country, countryJsonElement));
            data.setEmail(email);
            data.setPassword(password);
            data.setType(type);
            data.setCountryPortalId(countryPortalId);
            Call<JsonElement> call = oauthRepository.postRegisterUserFront(data);
            call.enqueue(new Callback<JsonElement>() {

                @EverythingIsNonNull
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    JsonElement jsonElement = response.body();
                    if (jsonElement == null) {
                        return;
                    }
                    try {
                        int responseInt = jsonElement.getAsInt();
                        View view = ViewInstanceList.getDictionaryViews("login-email-registro-fragment");
                        if (responseInt == StateSignUpUserNoAuth.UNREGISTERED.ordinal()) {
                            TextView tvCompleteFieldList = view.findViewById(R.id.textView);
                            EditText etEmail = view.findViewById(R.id.IdEditTextCorreo);
                            tvCompleteFieldList.setVisibility(View.VISIBLE);
                            if (Query.getPortalId() == 1) {
                                tvCompleteFieldList.setText(R.string.app_en_campos_completados);
                            } else {
                                tvCompleteFieldList.setText(R.string.app_es_campos_completados);
                            }
                            etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion), null);
                            new android.os.Handler().postDelayed(
                                    () -> {
                                        tvCompleteFieldList.setVisibility(View.INVISIBLE);
                                        etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                    }, 3000);
                        } else if (responseInt == StateSignUpUserNoAuth.REGISTERED.ordinal()) {
                            callback.onSuccess();
                        } else {
                            callback.onError(responseInt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                }

            });
        }
    }

    public void registerUserOAuth(String name, String email, String password, String confirmPassword, String country, int type, int countryPortalId, Callback<JsonElement> callback, LoginViewModel.LoginCallback loginCallback) {
        LoginRegisterData data = new LoginRegisterData();
        if (validatePassword(password, confirmPassword)) {
            data.setName(name);
            data.setCountryCodeTwo(country);
            data.setEmail(email);
            data.setPassword(password);
            data.setType(type);
            data.setCountryPortalId(countryPortalId);
            Call<JsonElement> call = oauthRepository.postRegisterUserFront(data);
            call.enqueue(callback);
        }
    }

    public String countryCodeTwo(String country, JsonElement countryJsonElement) {
        if (!countryJsonElement.isJsonNull()) {
            for (int i = 0; i < countryJsonElement.getAsJsonArray().size(); i++) {
                if (i == countryJsonElement.getAsJsonArray().size()) break;
                String countryName = countryJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisNombre").toString().replaceAll("\"", "");
                String countryCodeTwo = countryJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisCodigoDos").toString().replaceAll("\"", "");
                if ((country).equals(countryName)) return countryCodeTwo;
            }
        }
        return "";
    }

    public void countryListFront(Callback<JsonElement> calling) {
        CountryData data = new CountryData(Query.getPortalId());
        Call<JsonElement> call = oauthRepository.postCountryListFront(data);
        call.enqueue(calling);
    }

    public void postCountryListUserFront(Callback<JsonElement> calling) {
        CountryData data = new CountryData(UtilUser.getUser().getPortalId());
        Call<JsonElement> call = oauthRepository.postCountryListFront(data);
        call.enqueue(calling);
    }

    public void postCountryListUserFront3(Callback<JsonElement> calling, CountryData data) {
        Call<JsonElement> call = oauthRepository.postCountryListFront(data);
        call.enqueue(calling);
    }

    public void addCountrySpinner2(Spinner spinner, ArrayList<Country> countries, int currentItem, View views) {
        ArrayAdapter<Country> adapter = new ArrayAdapter(views.getContext(), R.layout.item_spinner_2, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(currentItem);
    }

    public void addLanguageSpinner(Spinner spinner, String[] array, View views) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(views.getContext(), R.layout.item_spinner_3, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}