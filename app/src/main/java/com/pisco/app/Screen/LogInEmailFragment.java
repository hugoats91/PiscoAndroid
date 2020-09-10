package com.pisco.app.Screen;

import android.annotation.SuppressLint;
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
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.PiscoApplication;
import com.pisco.app.R;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.UtilAnalytics;
import com.pisco.app.Utils.UtilDialog;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.ViewModel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LogInEmailFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private TextView textView;
    private Switch simpleSwitch;

    public LogInEmailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_email, container, false);
        ViewInstanceList.setViewInstances("login-email-fragment", view);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        TextView tvRecoverPassword = view.findViewById(R.id.IDTextViewRecuperarPassword);
        tvRecoverPassword.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login_correo", "Clic", "¿Olvidaste tu contraseña?");
            Navigation.findNavController(v).navigate(R.id.action_loginEmailFragment_to_loginEmailPasswordRecuperarFragment);
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_loginEmailFragment_to_loginFragment);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.textView);
        TextView tvKeepSession = view.findViewById(R.id.IDTextViewMantenerSesion);
        TextView tvForgetPassword = view.findViewById(R.id.IDTextViewRecuperarPassword);
        TextView tvEnter = view.findViewById(R.id.IDButtonLoginEmail);
        EditText tvEmail = view.findViewById(R.id.IDEditTextLoginEmailCorreo);
        EditText tvPassword = view.findViewById(R.id.IDEditTextLoginEmailContrasena);
        tvEmail.setCompoundDrawables(null, null, null, null);
        tvPassword.setCompoundDrawables(null, null, null, null);
        if (Query.getPortalId() == 1) {
            textView.setText(R.string.app_en_activity_login_email_above_img);
            tvKeepSession.setText(R.string.app_en_activity_login_email_above_button_in);
            tvForgetPassword.setText(Html.fromHtml(getString(R.string.app_en_activity_login_email_above_button_login)));
            tvEnter.setText(R.string.app_en_activity_login_email_btn_login);
            tvEmail.setHint(R.string.app_en_correo);
            tvPassword.setHint(R.string.app_en_contrasena);
        } else {
            textView.setText(R.string.app_es_activity_login_email_above_img);
            tvKeepSession.setText(R.string.app_es_activity_login_email_above_button_in);
            tvForgetPassword.setText(Html.fromHtml(getString(R.string.app_es_activity_login_email_above_button_login)));
            tvEnter.setText(R.string.app_es_activity_login_email_btn_login);
            tvEmail.setHint(R.string.app_es_correo);
            tvPassword.setHint(R.string.app_es_contrasena);
        }
        tvForgetPassword.setPaintFlags(tvForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String user = "";
        String password = "";
        Context context = getContext();
        if (context != null) {
            user = Query.readValue("correo", context);
            password = Query.readValue("password", context);
        }
        tvEmail.setText(user);
        tvPassword.setText(password);
        simpleSwitch = view.findViewById(R.id.simpleSwitch);
        Button btnLogInEmail = view.findViewById(R.id.IDButtonLoginEmail);
        btnLogInEmail.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login_correo", "Boton", "Ingresar");
            String email = tvEmail.getText().toString();
            String password1 = tvPassword.getText().toString();
            boolean switchMantenerSesion = getsimpleSwitchValue();
            if (Query.validateEmail(email) && password1.length() >= 5) {
                tvEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tvPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                textView.setVisibility(View.INVISIBLE);
                ViewModelInstanceList.getLogInViewModelInstance().loginUser(LogInEmailFragment.this, email, password1, switchMantenerSesion, getContext(), new LoginViewModel.LoginCallback() {
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
                                                Navigation.findNavController(view).navigate(R.id.action_loginEmailFragment_to_inicioBienvenidoFragment);
                                            } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1) {
                                                Navigation.findNavController(view).navigate(R.id.action_loginEmailFragment_to_inicioFragment);
                                            } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 0) {
                                                Navigation.findNavController(view).navigate(R.id.action_loginEmailFragment_to_inicioFragment);
                                            } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 0) {
                                                Navigation.findNavController(view).navigate(R.id.action_loginEmailFragment_to_inicioFragment);
                                            } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 0) {
                                                Navigation.findNavController(view).navigate(R.id.action_loginEmailFragment_to_inicioFragment);
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
                        switch (type){
                            case 1:
                            case 2:
                                textView.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                String error = "";
                                if (Query.getPortalId() == 1) {
                                    error = getString(R.string.app_en_login_clasico_error);
                                } else {
                                    error = getString(R.string.app_es_login_clasico_error);
                                }
                                UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), error);
                                break;
                        }
                    }
                });
            } else {
                tvEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion), null);
                tvPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion), null);
                textView.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(
                        () -> {
                            textView.setVisibility(View.INVISIBLE);
                            tvEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            tvPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        }, 2500);
            }
        });
    }

    public boolean getsimpleSwitchValue() {
        return simpleSwitch.isChecked();
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