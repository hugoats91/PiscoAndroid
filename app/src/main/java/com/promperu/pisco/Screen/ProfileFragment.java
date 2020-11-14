package com.promperu.pisco.Screen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.promperu.pisco.Entity.Country;
import com.promperu.pisco.Enum.StateUser;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.LocalService.Entity.EntityStateOnboarding;
import com.promperu.pisco.LocalService.Entity.EntityUser;
import com.promperu.pisco.PiscoApplication;
import com.promperu.pisco.R;
import com.promperu.pisco.Remote.UpdateAvatarRemote;
import com.promperu.pisco.Screen.Dialogs.MenuDialogFragment;
import com.promperu.pisco.Screen.Dialogs.ProgressDialogFragment;
import com.promperu.pisco.Utils.AppConstantList;
import com.promperu.pisco.Utils.CircleTransform;
import com.promperu.pisco.Utils.Query;
import com.promperu.pisco.Utils.UtilAnalytics;
import com.promperu.pisco.Utils.UtilBitmap;
import com.promperu.pisco.Utils.UtilDialog;
import com.promperu.pisco.Utils.UtilUser;
import com.promperu.pisco.Utils.ViewInstanceList;
import com.promperu.pisco.Utils.ViewModelInstanceList;
import com.promperu.pisco.ViewModel.HomeViewModel;
import com.promperu.pisco.ViewModel.LiveData.CountryData;
import com.promperu.pisco.ViewModel.LiveData.LoginRegisterData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private final int MY_PERMISSIONS = 100;
    private final int SELECT_PICTURE = 300;

    private View view;
    private OnFragmentInteractionListener listener;
    private String[] arrLanguage = new String[2];
    private JsonElement countryListJsonElement;
    private Button btnOption;
    private ImageView imageview;
    private ArrayList<Country> countries = new ArrayList<Country>();
    EntityUser user;

    private String imgBase64 = "data:image/png;base64,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";

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_in_mi_perfil, container, false);
        ViewInstanceList.setViewInstances("in-perfil-fragment", view);
        Context context = getContext();
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Mi Perfil");
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        user = UtilUser.getUser();
        ImageView ivMenu = view.findViewById(R.id.IDMenuModal);
        ivMenu.setOnClickListener(v -> {
            DialogFragment newFragment = new MenuDialogFragment();
            newFragment.show(getChildFragmentManager(), "missiles");
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        user = UtilUser.getUser();
        ImageView ivBack = view.findViewById(R.id.ImageViewButtonBackPerfil);
        ivBack.setOnClickListener(v -> {
            String type = args.getString("type");
            if (type != null) {
                Navigation.findNavController(view).navigate(R.id.action_inicioFragment);
            } else {
                UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Mi Perfil", "Boton", "Flecha-Regreso al juego");
                requireActivity().onBackPressed();
            }
        });
        HomeViewModel homeViewModel = new HomeViewModel();
        imageview = view.findViewById(R.id.IdImagePerfil);
        imageview.setDrawingCacheEnabled(true);
        LinearLayout linRectangle = view.findViewById(R.id.IDLinearLayoutRectanguleShadow);
        RelativeLayout relCountry = view.findViewById(R.id.IdRelativeContentPais);
        TextView tvValidationPassword = view.findViewById(R.id.IDapp_en_contrasena_validacion);
        btnOption = view.findViewById(R.id.show_options_button);
        TextView tvUserPassword = view.findViewById(R.id.IDUsuaPasswords);
        TextView tvGeneralInfo = view.findViewById(R.id.IDapp_es_informacion_general);
        TextView tvEmailProfile = view.findViewById(R.id.IDapp_es_correo_perfil);
        TextView tvCountryProfile = view.findViewById(R.id.IDapp_es_pais_perfil);
        TextView tvPassword = view.findViewById(R.id.IDapp_es_contrasena);
        TextView tvEdit = view.findViewById(R.id.IDapp_es_editar);
        ImageView tvCamera = view.findViewById(R.id.IDImageViewCamera);
        TextView tvLanguageProfile = view.findViewById(R.id.IDapp_es_idioma_perfil);
        TextView tvLanguage = view.findViewById(R.id.IDTextViewIdioma);
        if (user.getPortalId() == 0) {
            tvGeneralInfo.setText(getString(R.string.app_es_informacion_general));
            tvEmailProfile.setText(getString(R.string.app_es_correo_perfil));
            tvCountryProfile.setText(getString(R.string.app_es_pais_perfil));
            tvPassword.setText(getString(R.string.app_es_contrasena));
            tvEdit.setText(getString(R.string.app_es_editar));
            tvLanguageProfile.setText(getString(R.string.app_es_idioma_perfil));
            tvLanguage.setText(getString(R.string.app_es));
            tvValidationPassword.setText(getString(R.string.app_es_contrasena_validacion));
            arrLanguage[0] = getString(R.string.app_es_espanol);
            arrLanguage[1] = getString(R.string.app_es_ingles);
        } else {
            tvGeneralInfo.setText(getString(R.string.app_en_informacion_general));
            tvEmailProfile.setText(getString(R.string.app_en_correo_perfil));
            tvCountryProfile.setText(getString(R.string.app_en_pais_perfil));
            tvPassword.setText(getString(R.string.app_en_contrasena));
            tvEdit.setText(getString(R.string.app_en_editar));
            tvLanguageProfile.setText(getString(R.string.app_en_idioma_perfil));
            tvLanguage.setText(getString(R.string.app_en));
            tvValidationPassword.setText(getString(R.string.app_en_contrasena_validacion));
            arrLanguage[0] = getString(R.string.app_en_ingles);
            arrLanguage[1] = getString(R.string.app_en_espanol);
        }
        tvGeneralInfo.setPaintFlags(tvGeneralInfo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView tvName = view.findViewById(R.id.IDTextViewNombre);
        TextView tvPoints = view.findViewById(R.id.IDTextViewPuntos);
        TextView tvEmail = view.findViewById(R.id.IDTextViewCorreo);
        TextView tvCountry = view.findViewById(R.id.IDTextViewPais);
        TextView tvPassword2 = view.findViewById(R.id.IDTextViewPassword);
        EditText etName = view.findViewById(R.id.IDEditTextNombre);
        EditText etEmail = view.findViewById(R.id.IDEditTextCorreo);
        EditText etPassword = view.findViewById(R.id.IDEditTextPassword);
        Spinner spiCountry = view.findViewById(R.id.IdEditTextPais);

        homeViewModel.postUserDataFront(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement data = response.body();
                if (data == null) {
                    return;
                }
                try {
                    JsonObject jsonObject = data.getAsJsonObject();
                    int userId = jsonObject.get("UsuaId").getAsInt();
                    String userName = jsonObject.get("UsuaNombre").getAsString();
                    String userEmail = jsonObject.get("UsuaCorreo").getAsString();
                    String countryCodeTwo = jsonObject.get("PaisCodigoDos").getAsString();
                    String userImage = jsonObject.get("UsuaImagen").getAsString();
                    String userPassword = jsonObject.get("UsuaPassword").getAsString();
                    tvName.setText(userName);
                    tvEmail.setText(userEmail);
                    tvUserPassword.setText(userPassword);
                    if (userImage.equals("")) {
                        imageview.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.imagenperfil));
                    } else {
                        String urlImagen = user.getImagePath() + AppConstantList.RUTA_USUARIO + userId + "/" + userImage;
                        Picasso.get().load(urlImagen)
                                .transform(new CircleTransform())
                                .error(R.drawable.imagenperfil)
                                .placeholder(R.drawable.imagenperfil)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE).into(imageview);
                    }
                    ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().postCountryListUserFront(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            countryListJsonElement = response.body();
                            if (countryListJsonElement == null) {
                                return;
                            }
                            try {
                                int currentItem  = 0;
                                for (int i = 0; i < countryListJsonElement.getAsJsonArray().size(); i++) {
                                    if (i == countryListJsonElement.getAsJsonArray().size()) break;
                                    String codeTwo = countryListJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisCodigoDos").getAsString();
                                    if (countryCodeTwo.equals(codeTwo)) {
                                        currentItem = i;
                                        String countryName = countryListJsonElement.getAsJsonArray().get(i).getAsJsonObject().get("PaisNombre").getAsString();
                                        tvCountry.setText(countryName);
                                    }
                                    JsonObject json = countryListJsonElement.getAsJsonArray().get(i).getAsJsonObject();
                                    countries.add(new Country(json.get("PaisNombre").toString().replaceAll("\"", ""),
                                            json.get("PaisCodigoDos").toString().replaceAll("\"", ""),
                                            json.get("PaisPortalId").getAsInt(),
                                            json.get("PaisId").getAsInt()));
                                }
                                ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().addCountrySpinner2(spiCountry, countries, currentItem, view);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }

        });

        homeViewModel.currentScore(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                if (jsonElement == null) {
                    return;
                }
                try {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    int partPuntajeAcumulado = jsonObject.get("PartPuntajeAcumulado").getAsInt();
                    String pointText = partPuntajeAcumulado + " pts.";
                    tvPoints.setText(pointText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }

        });
        tvEdit.setOnClickListener(v -> {
            if (tvName.getVisibility() == View.GONE) {
                if ((etPassword.getText().toString().length() < 6 && etPassword.getText().toString().length() > 0)) {
                    tvValidationPassword.setVisibility(View.VISIBLE);
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, AppCompatResources.getDrawable(view.getContext(), R.drawable.alertasesion), null);
                    new android.os.Handler().postDelayed(
                            () -> {
                                tvValidationPassword.setVisibility(View.INVISIBLE);
                                etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            }, 3000);
                } else {
                    tvName.setVisibility(View.VISIBLE);
                    tvEmail.setVisibility(View.VISIBLE);
                    tvPassword2.setVisibility(View.VISIBLE);
                    tvCountry.setVisibility(View.VISIBLE);
                    tvLanguage.setVisibility(View.VISIBLE);
                    tvLanguageProfile.setVisibility(View.VISIBLE);
                    etName.setVisibility(View.GONE);
                    etEmail.setVisibility(View.GONE);
                    etPassword.setVisibility(View.GONE);
                    relCountry.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linRectangle.getLayoutParams();
                    params.height = params.height - 300;
                    linRectangle.requestLayout();
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String password;
                    if (etPassword.getText().toString().equals("")) {
                        password = tvUserPassword.getText().toString();
                    } else {
                        password = etPassword.getText().toString();
                    }
                    String country = spiCountry.getSelectedItem().toString();
                    ((TextView) view.findViewById(R.id.IDTextViewNombre)).setText(name);
                    ((TextView) view.findViewById(R.id.IDTextViewCorreo)).setText(email);
                    ((TextView) view.findViewById(R.id.IDTextViewPais)).setText(country);
                    int portalId = user.getPortalId();
                    if (portalId == 0) {
                        tvGeneralInfo.setText(getString(R.string.app_es_informacion_general));
                        tvEmailProfile.setText(getString(R.string.app_es_correo_perfil));
                        tvCountryProfile.setText(getString(R.string.app_es_pais_perfil));
                        tvPassword.setText(getString(R.string.app_es_contrasena));
                        tvEdit.setText(getString(R.string.app_es_editar));
                        tvLanguageProfile.setText(getString(R.string.app_es_idioma_perfil));
                        tvLanguage.setText(getString(R.string.app_es));
                        if (user.getPortalId() == 0) {
                            arrLanguage[0] = getString(R.string.app_es_espanol);
                            arrLanguage[1] = getString(R.string.app_es_ingles);
                        } else {
                            arrLanguage[0] = getString(R.string.app_es_ingles);
                            arrLanguage[1] = getString(R.string.app_es_espanol);
                        }
                    } else {
                        tvGeneralInfo.setText(getString(R.string.app_en_informacion_general));
                        tvEmailProfile.setText(getString(R.string.app_en_correo_perfil));
                        tvCountryProfile.setText(getString(R.string.app_en_pais_perfil));
                        tvPassword.setText(getString(R.string.app_en_contrasena));
                        tvEdit.setText(getString(R.string.app_en_editar));
                        tvLanguageProfile.setText(getString(R.string.app_en_idioma_perfil));
                        tvLanguage.setText(getString(R.string.app_en));
                        if (user.getPortalId() == 0) {
                            arrLanguage[0] = getString(R.string.app_en_espanol);
                            arrLanguage[1] = getString(R.string.app_en_ingles);
                        } else {
                            arrLanguage[0] = getString(R.string.app_en_ingles);
                            arrLanguage[1] = getString(R.string.app_en_espanol);
                        }
                    }
                    int code = ((Country) spiCountry.getSelectedItem()).getPaisId();
                    LoginRegisterData data = new LoginRegisterData(code, name, "", email, password, portalId, imgBase64);
                    String finalPassword = password;
                    UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Mi Perfil", "Boton", "Editar");
                    ProgressDialogFragment progress = UtilDialog.showProgress(this);
                    ViewModelInstanceList.getHomeViewModelInstance().postUpdateUserFront(new Callback<JsonElement>() {

                        @EverythingIsNonNull
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            progress.dismiss();
                            LoginRegisterData loginRegisterData = new LoginRegisterData();
                            loginRegisterData.setEmail(email);
                            loginRegisterData.setPassword(finalPassword);
                            loginRegisterData.setType(user.getUserType());
                            ViewModelInstanceList.getHomeViewModelInstance().postEmailSelectionFront(loginRegisterData, new Callback<JsonElement>() {
                                @Override
                                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                    ViewModelInstanceList.getHomeViewModelInstance().postGetCityListFront(new Callback<ArrayList<JsonObject>>() {
                                        @EverythingIsNonNull
                                        @Override
                                        public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> responseList) {
                                            ArrayList<JsonObject> arrCity = responseList.body();
                                            if (arrCity != null) {
                                                Query.saveCityCount(getContext(), arrCity.size());

                                                JsonElement jsonElement = response.body();
                                                if (jsonElement == null) {
                                                    return;
                                                }
                                                try {
                                                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                                                    String token = jsonObject.get("token").getAsString();
                                                    int userType = jsonObject.get("TipoUsuario").getAsInt();
                                                    int responseCode = jsonObject.get("RespuestaConsulta").getAsInt();
                                                    String userName = jsonObject.get("NombreUsuario").getAsString();
                                                    int userState = jsonObject.get("EstadoUsuario").getAsInt();
                                                    int welcomeState = jsonObject.get("EstadoBienvenido").getAsInt();
                                                    int initialScore = jsonObject.get("puntageInicio").getAsInt();
                                                    int baseScore = jsonObject.get("puntageBase").getAsInt();
                                                    int currentScore = jsonObject.get("puntageAcumulado").getAsInt();
                                                    String learnPisco = jsonObject.get("aprendePisco").getAsString();
                                                    int roulette = jsonObject.get("nroRuleta").getAsInt();
                                                    String imageRoulette = jsonObject.get("imagenRuleta").getAsString();
                                                    String imagePath = jsonObject.get("rutaImagen").getAsString();
                                                    int portalId = jsonObject.get("PortalId").getAsInt();
                                                    JsonArray stateOnboardingJsonArray = jsonObject.getAsJsonArray("listaEstadoOnnboarding");
                                                    if (responseCode == StateUser.EXISTS.ordinal()) {
                                                        AppDatabase.INSTANCE.userDao().deleteAll();
                                                        AppDatabase.INSTANCE.userDao().deleteAllEntityStateOnboarding();
                                                        EntityUser entityUser = new EntityUser(email, token, userType, responseCode, userName, userState, welcomeState, initialScore, baseScore, currentScore, learnPisco, roulette, imageRoulette, imagePath, portalId);
                                                        for (int i = 0; i < stateOnboardingJsonArray.size(); i++) {
                                                            if (i == stateOnboardingJsonArray.size())
                                                                break;
                                                            int sessionState = stateOnboardingJsonArray.get(i).getAsJsonObject().get("SesiEstado").getAsInt();
                                                            int pageId = stateOnboardingJsonArray.get(i).getAsJsonObject().get("PagiId").getAsInt();
                                                            String pageName = stateOnboardingJsonArray.get(i).getAsJsonObject().get("PagiNombre").getAsString();
                                                            EntityStateOnboarding entityStateOnboarding = new EntityStateOnboarding(sessionState, pageId, pageName);
                                                            AppDatabase.INSTANCE.userDao().insertStateOnboarding(entityStateOnboarding);
                                                        }
                                                        AppDatabase.INSTANCE.userDao().insert(entityUser);
                                                    }
                                                    if (Query.getPortalId() == 1) {
                                                        UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), getString(R.string.app_en_actualizar_perfil));
                                                    } else {
                                                        UtilDialog.infoMessage(requireContext(), getString(R.string.app_name), getString(R.string.app_es_actualizar_perfil));
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @EverythingIsNonNull
                                        @Override
                                        public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                                            progress.dismiss();
                                        }

                                    });

                                }

                                @Override
                                public void onFailure(Call<JsonElement> call, Throwable t) {
                                    progress.dismiss();
                                }

                            });
                        }

                        @EverythingIsNonNull
                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {
                            progress.dismiss();
                        }

                    }, data);
                }
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linRectangle.getLayoutParams();
                params.height = params.height + 300;
                linRectangle.requestLayout();
                tvName.setVisibility(View.GONE);
                tvEmail.setVisibility(View.GONE);
                tvPassword2.setVisibility(View.GONE);
                tvCountry.setVisibility(View.GONE);
                etName.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                relCountry.setVisibility(View.VISIBLE);
                etName.setText(tvName.getText());
                etEmail.setText(tvEmail.getText());
                etPassword.setText("");
                if (user.getUserType() != 0) {
                    etEmail.setEnabled(false);
                    etPassword.setEnabled(false);
                    spiCountry.setEnabled(false);
                } else {
                    etEmail.setEnabled(true);
                    etPassword.setEnabled(true);
                    spiCountry.setEnabled(true);
                }
                if (user.getPortalId() == 1) {
                    etName.setHint(getString(R.string.app_en_nombre));
                    etEmail.setHint(getString(R.string.app_en_correo));
                    etPassword.setHint(getString(R.string.app_en_contrasena));
                    tvEdit.setText(getString(R.string.app_en_guardar));
                } else {
                    etName.setHint(getString(R.string.app_es_nombre));
                    etEmail.setHint(getString(R.string.app_es_correo));
                    etPassword.setHint(getString(R.string.app_es_contrasena));
                    tvEdit.setText(getString(R.string.app_es_guardar));
                }

            }
        });
        tvCamera.setOnClickListener(v -> {
            if (user.getUserType() == 0) {
                requestPermissions();
            }
        });
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            showOptions();
        if ((ContextCompat.checkSelfPermission(view.getContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(view.getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED)){
            showOptions();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    imageview.setImageBitmap(UtilBitmap.getCroppedBitmap(imageBitmap));
                    updateAvatar(UtilBitmap.getBase64ByBitmap(imageBitmap));
                }
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                Bitmap imageBitmap = getThumbnail(path);
                Picasso.get().load(data.getData())
                        .transform(new CircleTransform())
                        .error(R.drawable.imagenperfil)
                        .placeholder(R.drawable.imagenperfil)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE).into(imageview);

                updateAvatar(UtilBitmap.getBase64ByBitmap(imageBitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int CAMERA_PIC_REQUEST = 1337;
        if (requestCode == CAMERA_PIC_REQUEST && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap image = (Bitmap) extras.get("data");
                imageview.setImageBitmap(image);
            }
        }
        int PHOTO_CODE = 200;
        if (requestCode == PHOTO_CODE) {
            String path1 = "";
            MediaScannerConnection.scanFile(view.getContext(), new String[]{path1}, null, (path, uri) -> {
            });
            Bitmap bitmap = BitmapFactory.decodeFile(path1);
            imageview.setImageBitmap(bitmap);
        }
    }

    public void updateAvatar(String avatar) {
        UpdateAvatarRemote data = new UpdateAvatarRemote("data:image/jpeg;base64," + avatar);
        ProgressDialogFragment progress = UtilDialog.showProgress(this);
        ViewModelInstanceList.getHomeViewModelInstance().postUpdateAvatarFront(new Callback<JsonElement>() {

            @EverythingIsNonNull
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progress.dismiss();
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progress.dismiss();
            }

        }, data);
    }


    public Bitmap getThumbnail(Uri uri) throws IOException {
        InputStream input = view.getContext().getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null) {
            input.close();
        }
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }
        int originalSize = Math.max(onlyBoundsOptions.outHeight, onlyBoundsOptions.outWidth);
        double THUMBNAIL_SIZE = 100f;
        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = view.getContext().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        if (input != null) {
            input.close();
        }
        return bitmap;
    }

    private int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showOptions();
            }else{
                if(Query.getPortalId() == 1){
                    UtilDialog.infoTwoOptionsMessage(requireContext(), getString(R.string.app_name), getString(R.string.app_en_aceptar_permisos), this::requestPermissions, () -> Navigation.findNavController(view).popBackStack());
                }else{
                    UtilDialog.infoTwoOptionsMessage(requireContext(), getString(R.string.app_name), getString(R.string.app_es_aceptar_permisos), this::requestPermissions, () -> Navigation.findNavController(view).popBackStack());
                }
            }
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



    @SuppressLint("IntentReset")
    private void showOptions() {
        String takePhoto;
        String chooseGallery;
        String cancel;
        String chooseOption;
        String selectImage;
        if (user.getPortalId() == 0) {
            takePhoto = getString(R.string.app_es_tomar_foto);
            chooseGallery = getString(R.string.app_es_elegir_galeria);
            cancel = getString(R.string.app_es_cancelar);
            chooseOption = getString(R.string.app_es_elegir_opcion);
            selectImage = getString(R.string.app_es_selecciona_app_imagen);
        } else {
            takePhoto = getString(R.string.app_en_tomar_foto);
            chooseGallery = getString(R.string.app_en_elegir_galeria);
            cancel = getString(R.string.app_en_cancelar);
            chooseOption = getString(R.string.app_en_elegir_opcion);
            selectImage = getString(R.string.app_en_selecciona_app_imagen);
        }
        final CharSequence[] option = {takePhoto, chooseGallery, cancel};
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(chooseOption);
        String finalTakePhoto = takePhoto;
        String finalChooseGallery = chooseGallery;
        String finalSelectImage = selectImage;
        builder.setItems(option, (dialog, which) -> {
            if (option[which] == finalTakePhoto) {
                openCamera();
            } else if (option[which] == finalChooseGallery) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/jpeg");
                startActivityForResult(Intent.createChooser(intent, finalSelectImage), SELECT_PICTURE);
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void showExplanation() {
        String deniedPermissions;
        String acceptPermissions;
        String accept;
        String cancel;
        if (user.getPortalId() == 1) {
            deniedPermissions = getString(R.string.app_en_permisos_denegados);
            acceptPermissions = getString(R.string.app_en_aceptar_permisos);
            accept = getString(R.string.app_en_aceptar);
            cancel = getString(R.string.app_en_cancelar);
        } else {
            deniedPermissions = getString(R.string.app_es_permisos_denegados);
            acceptPermissions = getString(R.string.app_es_aceptar_permisos);
            accept = getString(R.string.app_es_aceptar);
            cancel = getString(R.string.app_es_cancelar);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(deniedPermissions);
        builder.setMessage(acceptPermissions);
        builder.setPositiveButton(accept, (dialog, which) -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            String packageName = view.getContext().getPackageName();
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton(cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
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