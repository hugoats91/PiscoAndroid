package com.pisco.app.ViewModel;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.google.gson.JsonElement;
import com.pisco.app.Entity.Country;
import com.pisco.app.Enum.StateSignUpUserNoAuth;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Repository.OAuthRepository;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.LiveData.LoginRegisterData;
import com.pisco.app.ViewModel.LiveData.CountryData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LogInEmailRegisterViewModel extends ViewModel {

    private OAuthRepository oauthRepository = new OAuthRepository();
    private JsonElement countryJsonElement = null;
    private String[] arrayCountry;

    public interface RegisterCallback{
        void onSuccess();
        void onError(int type);
    }

    public boolean validatePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public void registerUser(String name, String email, String password, String confirmPassword, String country, int type, int countryPortalId, RegisterCallback callback) {
        LoginRegisterData data = new LoginRegisterData();
        if (validatePassword(password, confirmPassword)) {
            data.setName(name);
            data.setCountryCodeTwo(countryCodeTwo(country));
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
                            etEmail.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(),R.drawable.alertasesion),null);
                            new android.os.Handler().postDelayed(
                                    () -> {
                                        tvCompleteFieldList.setVisibility(View.INVISIBLE);
                                        etEmail.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                                    }, 3000);
                        } else if (responseInt == StateSignUpUserNoAuth.REGISTERED.ordinal()) {
                            callback.onSuccess();
                        }else{
                            callback.onError(responseInt);
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

    public String countryCodeTwo(String country) {
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

    public String countryName(String countryCodeTwo) {
        if (!countryJsonElement.isJsonNull()) {
            for (int i = 0; i < countryJsonElement.getAsJsonArray().size(); i++) {
                if (i == countryJsonElement.getAsJsonArray().size()) break;
                String countryName = countryJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisNombre").toString().replaceAll("\"", "");
                String countryCodeTwo2 = countryJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisCodigoDos").toString().replaceAll("\"", "");
                if((countryCodeTwo).equals(countryCodeTwo2)) return countryName;
            }
        }
        return "";
    }

    public void addCountrySpinner(View view, final Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, arrayCountry);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
    }

    public void countryListFront(Context context) {
        CountryData data = new CountryData(Query.getPortalId());
        Call<JsonElement> call = oauthRepository.postCountryListFront(data);
        call.enqueue(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                countryJsonElement = response.body();
                if (countryJsonElement == null) {
                    return;
                }
                try {
                    arrayCountry = new String[countryJsonElement.getAsJsonArray().size()+1];
                    String countryCodeTwo = Query.getUserCountry(context);
                    String countryName = countryName(countryCodeTwo);
                    arrayCountry[0] = countryName.replaceAll("\"", "");
                    for (int i = 0; i < countryJsonElement.getAsJsonArray().size(); i++) {
                        if (i== countryJsonElement.getAsJsonArray().size()) break;
                        arrayCountry[i + 1] = countryJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisNombre").toString().replaceAll("\"", "");
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

    public void postCountryListUserFront(Callback<JsonElement> calling){
        CountryData data = new CountryData(AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId());
        Call<JsonElement> call = oauthRepository.postCountryListFront(data);
        call.enqueue(calling);
    }

    public void postCountryListUserFront3(Callback<JsonElement> calling, CountryData data) {
        Call<JsonElement> call = oauthRepository.postCountryListFront(data);
        call.enqueue(calling);
    }

    public void addCountrySpinner2(Spinner spinner, ArrayList<Country> countries, View views) {
        ArrayAdapter<Country> adapter = new ArrayAdapter(views.getContext(), R.layout.item_spinner_2, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void addLanguageSpinner(Spinner spinner, String[] array, View views) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(views.getContext(), R.layout.item_spinner_3, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}