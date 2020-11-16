package com.promperu.pisco.Screen.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.PiscoApplication;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.Query;
import com.promperu.pisco.Utils.UtilAnalytics;
import com.promperu.pisco.Utils.UtilDialog;
import com.promperu.pisco.Utils.UtilUser;

public class MenuDialogFragment extends DialogFragment {

    public MenuDialogFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.myFullscreenAlertDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void exit() {
        Context context = getContext();
        if (context != null){
            Query.clearValuePreferences(context);
        }
        dismiss();
        Fragment fragment = getParentFragment();
        if (fragment == null){
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }
        Navigation.findNavController(view).navigate(R.id.loginFragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Menu");
        ImageView ivClose = view.findViewById(R.id.ImageViewButtonMenuClose);
        ivClose.setOnClickListener(v -> dismiss());
        view.findViewById(R.id.IdCardViewPerfil).setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Menu", "Boton", "Menu - Perfil");
            dismiss();
            Fragment fragment = getParentFragment();
            if (fragment == null){
                return;
            }
            View fragmentView = fragment.getView();
            if (fragmentView == null) {
                return;
            }
            Navigation.findNavController(fragmentView).navigate(R.id.in_mi_perfil);
        });

        view.findViewById(R.id.IdCardViewJugar).setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Menu", "Boton", "Menu - Jugar");
            dismiss();
            Fragment fragment = getParentFragment();
            if (fragment == null){
                return;
            }
            View fragmentView = fragment.getView();
            if (fragmentView == null) {
                return;
            }
            Navigation.findNavController(fragmentView).navigate(R.id.inicioFragment);
        });

        view.findViewById(R.id.IDCardViewDondeCompra).setOnClickListener(v -> {
            if(Query.readValueInt("count", getContext())>0){
                UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Menu", "Boton", "Menu - ¿Donde comprar?");
                dismiss();
                Fragment fragment = getParentFragment();
                if (fragment == null){
                    return;
                }
                View fragmentView = fragment.getView();
                if (fragmentView == null) {
                    return;
                }
                Navigation.findNavController(fragmentView).navigate(R.id.in_donde_comprar);
            }else{
                if (UtilUser.getUser().getPortalId() == 0) {
                    UtilDialog.infoMessage(getContext(), getString(R.string.app_name), getString(R.string.app_es_no_puntos_venta_aviso), () -> {
                        dismiss();
                    });
                }else{
                    UtilDialog.infoMessage(getContext(), getString(R.string.app_name), getString(R.string.app_en_no_puntos_venta_aviso), () -> {
                        dismiss();
                    });
                }
            }
        });

        view.findViewById(R.id.IDCardViewSobrePisco).setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Menu", "Boton", "Menu - Aprende sobre Pisco");
            String url = UtilUser.getUser().getLearnPisco();
            if(!url.isEmpty()){
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.IDCardViewReceta).setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Menu", "Boton", "Menu - Recetas");
            dismiss();
            Fragment fragment = getParentFragment();
            if (fragment == null){
                return;
            }
            View fragmentView = fragment.getView();
            if (fragmentView == null) {
                return;
            }
            Navigation.findNavController(fragmentView).navigate(R.id.in_receta);
        });

        view.findViewById(R.id.IDCardViewLogin).setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Menu", "Boton", "Menu - Cerrar Sesion");
            int type = Query.readValueInt("login_type", requireContext());
            switch (type){
                case 0:
                    exit();
                    break;
                case 1:
                    disconnectFromFacebook(this::exit);
                    break;
                case 2:
                    Query.googleSignInClient.signOut().addOnCompleteListener(task -> exit());
                    break;
            }
        });

        TextView tvProfile = view.findViewById(R.id.id_app_es_menu_perfil);
        TextView tvPlay = view.findViewById(R.id.id_app_es_menu_jugar);
        TextView tvRecipe = view.findViewById(R.id.id_app_es_menu_receta);
        TextView tvBuyLocationList = view.findViewById(R.id.id_app_es_menu_donde_comprar);
        TextView tvLearnPisco = view.findViewById(R.id.IDDialogMenuAPRENDE);
        TextView tvSignOff = view.findViewById(R.id.id_app_es_menu_salir);

        if (UtilUser.getUser().getPortalId() == 1) {
            tvProfile.setText(R.string.app_en_menu_perfil);
            tvPlay.setText(R.string.app_en_menu_jugar);
            tvRecipe.setText(R.string.app_en_menu_receta);
            tvBuyLocationList.setText(R.string.app_en_menu_donde_comprar);
            tvLearnPisco.setText(R.string.app_en_menu_aprende_sobre_pisco);
            tvSignOff.setText(R.string.app_en_menu_salir);
        } else {
            tvProfile.setText(R.string.app_es_menu_perfil);
            tvPlay.setText(R.string.app_es_menu_jugar);
            tvRecipe.setText(R.string.app_es_menu_receta);
            tvBuyLocationList.setText(R.string.app_es_menu_donde_comprar);
            tvLearnPisco.setText(R.string.app_es_menu_aprende_sobre_pisco);
            tvSignOff.setText(R.string.app_es_menu_salir);
        }
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