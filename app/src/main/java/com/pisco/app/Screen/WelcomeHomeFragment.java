package com.pisco.app.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.PiscoApplication;
import com.pisco.app.R;
import com.pisco.app.Screen.Dialogs.OnboardDialogFragment;
import com.pisco.app.Screen.Dialogs.ProgressDialogFragment;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.UtilAnalytics;
import com.pisco.app.Utils.UtilDialog;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.ViewModel.WelcomeViewModel;

import org.jetbrains.annotations.NotNull;

public class WelcomeHomeFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    public WelcomeHomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_inicio_bienvenido, container, false);
        ViewInstanceList.setViewInstances("inicio-bienvenido-fragment",view);
        Context context = getContext();
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Login Registro");
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        Button btnContinue = view.findViewById(R.id.IDButtonContinuarRegisterExitoso);
        btnContinue.setOnClickListener(v -> ViewModelInstanceList.getWelcomeViewModelInstance().updateWelcomeFront(() -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "sign_up", "Registro", "Continuar");
            ProgressDialogFragment dialog = UtilDialog.showProgress(this);
            if (AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.1").getSesiState() == 1 && AppDatabase.INSTANCE.userDao().getEntityStateOnboarding("OnBoard 1.2").getSesiState() == 1) {
                Navigation.findNavController(view).navigate(WelcomeHomeFragmentDirections.actionInicioBienvenidoFragmentToOnBoardFragment("onBoard-inicio"));
            }else{
                Navigation.findNavController(view).navigate(R.id.action_inicioBienvenidoFragment_to_inicioFragment);
            }
        }));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvContinue = view.findViewById(R.id.IDButtonContinuarRegisterExitoso);
        TextView tvWelcome = view.findViewById(R.id.mensaje_cambio_contrasena_exitoso);
        if (Query.getPortalId() == 1) {
            tvContinue.setText(R.string.app_en_continuar);
            tvWelcome.setText(R.string.app_en_mensaje_cambio_contrasena_exitoso);
        } else {
            tvContinue.setText(R.string.app_es_continuar);
            tvWelcome.setText(R.string.app_es_mensaje_cambio_contrasena_exitoso);
        }
        autogenerate();
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }
    public void autogenerate() {
        TextView tvChangePasswordSuccessfull = ViewInstanceList.getDictionaryViews("inicio-bienvenido-fragment").findViewById(R.id.mensaje_cambio_contrasena_exitoso);
        String changePasswordSuccessfull = (String) tvChangePasswordSuccessfull.getText();
        String userName = AppDatabase.INSTANCE.userDao().getEntityUser().getUserName();
        changePasswordSuccessfull = changePasswordSuccessfull.replaceAll("nombre!", "<b>"+userName+"</b>!");
        tvChangePasswordSuccessfull.setText(Html.fromHtml(changePasswordSuccessfull));
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