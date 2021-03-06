package com.promperu.pisco.Screen;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.promperu.pisco.Entity.Country;
import com.promperu.pisco.Enum.UserType;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.PiscoApplication;
import com.promperu.pisco.R;
import com.promperu.pisco.Screen.Dialogs.ProgressDialogFragment;
import com.promperu.pisco.Utils.Query;
import com.promperu.pisco.Utils.UtilAnalytics;
import com.promperu.pisco.Utils.UtilDialog;
import com.promperu.pisco.Utils.UtilText;
import com.promperu.pisco.Utils.ViewModelInstanceList;
import com.promperu.pisco.Utils.ViewInstanceList;
import com.promperu.pisco.ViewModel.LogInEmailRegisterViewModel;
import com.promperu.pisco.ViewModel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LogInEmailRegisterFragment extends Fragment {

    private JsonElement countryListJsonElement;
    private ArrayList<Country> countries = new ArrayList<>();

    private String name = null;
    private String email = null;
    private String password = null;
    private String confirmPassword = null;
    private String country = null;
    private Spinner spinnerCountry = null;
    private OnFragmentInteractionListener listener;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView tvFillFields;

    public LogInEmailRegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_email_registro, container, false);
        ViewInstanceList.setViewInstances("login-email-registro-fragment",view);
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Login por Correo");
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etName = view.findViewById(R.id.IdEditTextNombre);
        etEmail = view.findViewById(R.id.IdEditTextCorreo);
        etPassword = view.findViewById(R.id.IdEditTextContrasena);
        etConfirmPassword = view.findViewById(R.id.IdEditTextContrasenaConfirmar);
        Button btnRegister = view.findViewById(R.id.IDButtonRegistroEmail);
        TextView tvHaveAccount = view.findViewById(R.id.IDTextViewLoginCreateUserYaTengoCuenta);
        tvFillFields = view.findViewById(R.id.textView);
        tvFillFields.setVisibility(View.GONE);
        spinnerCountry = view.findViewById(R.id.IdEditTextPais);

        ProgressDialogFragment frag  = UtilDialog.showProgress(this);
        ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().countryListFront( new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                frag.dismiss();
                countryListJsonElement = response.body();
                if (countryListJsonElement == null) {
                    return;
                }
                try {
                    JsonArray jsonArray = countryListJsonElement.getAsJsonArray();

                    //arrayCountry = new String[countryJsonElement.getAsJsonArray().size()+1];
                    //String countryCodeTwo = Query.getUserCountry(context);
                    //String countryName = countryName(countryCodeTwo);

                    String countryCodeTwo = Query.getUserCountry(getContext());
                    int currentItem = 0;

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject json = countryListJsonElement.getAsJsonArray().get(i).getAsJsonObject();
                        String countryCode = json.get("PaisCodigoDos").toString().replaceAll("\"", "");
                        if(countryCode.equals(countryCodeTwo)) currentItem = i;
                        countries.add(new Country(json.get("PaisNombre").toString().replaceAll("\"", ""),
                                countryCode,
                                json.get("PaisPortalId").getAsInt(),
                                json.get("PaisId").getAsInt()));
                    }
                    ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().addCountrySpinner2(spinnerCountry, countries, currentItem, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                frag.dismiss();
            }
        });

        if (Query.getPortalId() == 1) {
            etName.setHint(R.string.app_en_nombre);
            etEmail.setHint(R.string.app_en_correo);
            etPassword.setHint(R.string.app_en_contrasena);
            etConfirmPassword.setHint(R.string.app_en_contrasena_confirmar);
            btnRegister.setText(R.string.app_en_register);
            tvHaveAccount.setText(Html.fromHtml(getString(R.string.app_en_cuenta)));
            tvFillFields.setText(R.string.app_en_completar_campos_password);
        } else {
            etName.setHint(R.string.app_es_nombre);
            etEmail.setHint(R.string.app_es_correo);
            etPassword.setHint(R.string.app_es_contrasena);
            etConfirmPassword.setHint(R.string.app_es_contrasena_confirmar);
            btnRegister.setText(R.string.app_es_register);
            tvHaveAccount.setText(Html.fromHtml(getString(R.string.app_es_cuenta)));
            tvFillFields.setText(R.string.app_es_completar_campos_password);
        }
        tvHaveAccount.setPaintFlags(tvHaveAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnRegister.setOnClickListener(v -> {
            if (validate(view)) {
                etName.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                etEmail.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                etPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                country = spinnerCountry.getSelectedItem().toString();
                int userType = UserType.EMAIL.ordinal();
                int countryPortalId= Query.getPortalId();
                UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "sign_up", "Registro", "Registrate");
                ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().registerUser(name, email, password, confirmPassword, country, userType, countryPortalId, countryListJsonElement, new LogInEmailRegisterViewModel.RegisterCallback() {
                    @Override
                    public void onSuccess() {
                        ViewModelInstanceList.getLogInViewModelInstance().loginUser(LogInEmailRegisterFragment.this, email, password, false, getContext(), new LoginViewModel.LoginCallback() {
                            @Override
                            public void onSuccess(long result, int userState) {
                                ViewModelInstanceList.getHomeViewModelInstance().postGetCityListFront(new Callback<ArrayList<JsonObject>>() {
                                    @EverythingIsNonNull
                                    @Override
                                    public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> responseList) {
                                        ArrayList<JsonObject> arrCity = responseList.body();
                                        if (arrCity != null) {
                                            Query.saveCityCount(getContext(), arrCity.size());
                                            if (result > 0) {
                                                if (userState == 1) {
                                                    if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 1) {
                                                        Navigation.findNavController(view).navigate(R.id.action_loginEmailRegistroFragment_to_inicioBienvenidoFragment);
                                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1) {
                                                        Navigation.findNavController(view).navigate(R.id.action_loginEmailRegistroFragment_to_inicioFragment);
                                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 0) {
                                                        Navigation.findNavController(view).navigate(R.id.action_loginEmailRegistroFragment_to_inicioFragment);
                                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 0) {
                                                        Navigation.findNavController(view).navigate(R.id.action_loginEmailRegistroFragment_to_inicioFragment);
                                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 0) {
                                                        Navigation.findNavController(view).navigate(R.id.action_loginEmailRegistroFragment_to_inicioFragment);
                                                    }
                                                } else {
                                                    Navigation.findNavController(view).navigate(R.id.action_edit_profile);
                                                }
                                            } else {
                                                TextView textView = view.findViewById(R.id.textView);
                                                textView.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    @EverythingIsNonNull
                                    @Override
                                    public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

                                    }

                                });
                            }

                            @Override
                            public void onError(int type) {
                                UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), UtilText.errorRegister(type, requireContext()));
                            }
                        });
                    }

                    @Override
                    public void onError(int type) {
                        UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), UtilText.errorRegister(type, requireContext()));
                    }
                });
            } else {
                tvFillFields.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(
                        () -> {
                            tvFillFields.setVisibility(View.GONE);
                            etName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            etEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        }, 3000);
            }
        });
        tvHaveAccount.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
    }

    public Boolean validate(View view){
        try {
            name = (etName).getText().toString();
            email = (etEmail).getText().toString();
            password = (etPassword).getText().toString().trim();
            confirmPassword = (etConfirmPassword).getText().toString().trim();
            if(name.isEmpty()){
                etName.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                if (Query.getPortalId() == 1) {
                    tvFillFields.setText(R.string.app_en_completar_campos_password);
                }else{
                    tvFillFields.setText(R.string.app_es_completar_campos_password);
                }
                return false;
            }
            if (!Query.validateEmail(email)){
                etEmail.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                if (Query.getPortalId() == 1) {
                    tvFillFields.setText(R.string.app_en_validacion_email);
                }else{
                    tvFillFields.setText(R.string.app_es_validacion_email);
                }
                return false;
            }
            if (password.isEmpty()){
                etPassword.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                if (Query.getPortalId() == 1) {
                    tvFillFields.setText(R.string.app_en_completar_campos_password);
                }else{
                    tvFillFields.setText(R.string.app_es_completar_campos_password);
                }
                return false;
            }
            if (confirmPassword.isEmpty()){
                etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                if (Query.getPortalId() == 1) {
                    tvFillFields.setText(R.string.app_en_completar_campos_password);
                }else{
                    tvFillFields.setText(R.string.app_es_completar_campos_password);
                }
                return false;
            }
            if (!(password.length()>=5&& confirmPassword.length()>=5)){
                etPassword.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                if (Query.getPortalId() == 1) {
                    tvFillFields.setText(R.string.app_en_validacion_password);
                }else{
                    tvFillFields.setText(R.string.app_es_validacion_password);
                }
                return false;
            }
            if (!confirmPassword.equals(password)){
                etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion),null);
                if (Query.getPortalId() == 1) {
                    tvFillFields.setText(R.string.app_en_diferente_password);
                }else{
                    tvFillFields.setText(R.string.app_es_diferente_password);
                }
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

}