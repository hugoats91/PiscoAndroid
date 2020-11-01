package com.promperu.pisco.Screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
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
import com.promperu.pisco.ViewModel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@SuppressWarnings("deprecation")
public class LogInFragment extends Fragment  implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SIGN_IN_FACEBOOK = 64206;

    private OnFragmentInteractionListener listener;
    private View view;
    private CallbackManager callbackManager;


    public LogInFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            Query.googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        }
        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(this.getActivity().getApplication());
        callbackManager = CallbackManager.Factory.create();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ViewInstanceList.setViewInstances("login-fragment", view);

        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Login");

        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_self);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        Button btnLogIn = view.findViewById(R.id.btnLogin);
        btnLogIn.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login", "Boton", "Iniciar sesion por correo");
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_loginEmailFragment);
        });
        SignInButton signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            Intent signInIntent = Query.googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        Query.btnFacebook = view.findViewById(R.id.IDbtnFacebook);
        Query.btnFacebook.setBackgroundResource(R.drawable.facebook);
        Query.btnFacebook.setPermissions(Arrays.asList("public_profile", "email"));
        Query.btnFacebook.setFragment(this);
        Query.btnFacebook.setText("Login");
        Query.btnFacebook.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        Query.btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login", "Boton", "Facebook");
                final AccessToken accessToken = loginResult.getAccessToken();
                RequestFacebookGraph(accessToken, (user, graphResponse) -> {
                    try {
                        String name = graphResponse.getJSONObject().optString("name");
                        String password = graphResponse.getJSONObject().optString("id");
                        String confirmPassword = graphResponse.getJSONObject().optString("id");
                        String email = graphResponse.getJSONObject().optString("email");
                        String country = Query.getUserCountry(getContext());
                        int userType = UserType.FACEBOOK.ordinal();
                        int countryPortalId = Query.getPortalId();
                        ViewModelInstanceList.getLogInViewModelInstance().logInOAuthUser(LogInFragment.this, name, email, password, confirmPassword, country, userType, countryPortalId, getContext(), new LoginViewModel.LoginCallback() {
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
                                                if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 1) {
                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioBienvenidoFragment);
                                                } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1) {
                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                                } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 0) {
                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                                } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 0) {
                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                                } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 0) {
                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                                }
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
                                ProgressDialogFragment progress = UtilDialog.showProgress(LogInFragment.this);
                                disconnectFromFacebook(() -> {
                                    progress.dismiss();
                                    UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), UtilText.errorRegister(type, requireContext()));
                                });


                            }
                        });
                    } catch (Exception e) {
                        UtilDialog.infoMessage(getContext(), getString(R.string.app_name), "No se puede conectar con Facebook");
                    }
                });
            }

            @Override
            public void onCancel() {
                UtilDialog.infoMessage(getContext(), getString(R.string.app_name), "No se puede conectar con Facebook");
            }

            @Override
            public void onError(FacebookException exception) {
                UtilDialog.infoMessage(getContext(), getString(R.string.app_name), exception.getMessage());
            }
        });

        ImageButton ibFaccebook = view.findViewById(R.id.IDImageButtonbtnFacebook);
        ibFaccebook.setOnClickListener(v -> Query.btnFacebook.callOnClick());
        ImageButton btnGoogle = view.findViewById(R.id.IDbtnGoogle);
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = Query.googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        TextView textViewRegistrar = view.findViewById(R.id.textViewRegistrar);
        textViewRegistrar.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login", "Clic", "Registrate");
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_loginEmailRegistroFragment);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvLogIn = view.findViewById(R.id.textView);
        TextView tvLogInEmail = view.findViewById(R.id.btnLogin);
        TextView tvRegister = view.findViewById(R.id.textViewRegistrar);
        TextView tvO = view.findViewById(R.id.textViewO);
        if (Query.getPortalId() == 1) {
            tvLogIn.setText(R.string.app_en_iniciar_sesion);
            tvLogInEmail.setText(R.string.app_en_btn_login);
            tvRegister.setText(R.string.app_en_register);
            tvO.setText(R.string.app_en_o);
            tvRegister.setText(Html.fromHtml(getString(R.string.app_en_register)));
        } else {
            tvLogIn.setText(R.string.app_es_iniciar_sesion);
            tvLogInEmail.setText(R.string.app_es_btn_login);
            tvRegister.setText(R.string.app_es_register);
            tvO.setText(R.string.app_es_o);
            tvRegister.setText(Html.fromHtml(getString(R.string.app_es_register)));
        }
        tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String email = "";
        String password = "";
        Context context = getContext();
        if (context != null) {
            email = Query.readValue("correo", getContext());
            password = Query.readValue("password", getContext());
        }
        if (!email.isEmpty() && !password.isEmpty()) {
            Navigation.findNavController(view).navigate(R.id.inicioFragment);
        }
    }

    public void RequestFacebookGraph(AccessToken accessToken, GraphRequest.GraphJSONObjectCallback callback) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, callback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,id,first_name,link,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == RC_SIGN_IN_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(GoogleSignInAccount user) {
        if (user != null) {
            String name = user.getGivenName();
            String email = user.getEmail();
            String password = user.getId();
            String confirmPassword = user.getId();
            String country = Query.getUserCountry(getContext());
            int userType = UserType.GOOGLE.ordinal();
            int countryPortalId = Query.getPortalId();
            ViewModelInstanceList.getLogInViewModelInstance().logInOAuthUser(this, name, email, password, confirmPassword, country, userType, countryPortalId, getContext(), new LoginViewModel.LoginCallback() {
                @Override
                public void onSuccess(long result, int userState) {
                    UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login", "Boton", "Google");
                    ViewModelInstanceList.getHomeViewModelInstance().postGetCityListFront(new Callback<ArrayList<JsonObject>>() {
                        @EverythingIsNonNull
                        @Override
                        public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> responseList) {
                            ArrayList<JsonObject> arrCity = responseList.body();
                            if (arrCity != null) {
                                Query.saveCityCount(getContext(), arrCity.size());
                                if (result > 0) {
                                    if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 1) {
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioBienvenidoFragment);
                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1) {
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 0) {
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 0) {
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                    } else if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("Bienvenidos").getSesiState() == 0) {
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_inicioFragment);
                                    }
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
                    Query.googleSignInClient.signOut().addOnCompleteListener(task -> {
                        UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), UtilText.errorRegister(type, requireContext()));
                    });

                }
            });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

    interface FacebookLogout{
        void onSuccess();
    }

    public void disconnectFromFacebook(FacebookLogout callback) {

        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }
        ProgressDialogFragment progress = UtilDialog.showProgress(this);
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> {
            progress.dismiss();
            LoginManager.getInstance().logOut();
            callback.onSuccess();

        }).executeAsync();
    }

}