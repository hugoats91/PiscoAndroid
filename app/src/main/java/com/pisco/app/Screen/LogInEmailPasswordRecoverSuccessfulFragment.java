package com.pisco.app.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.pisco.app.PiscoApplication;
import com.pisco.app.R;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.UtilAnalytics;

import org.jetbrains.annotations.NotNull;

public class LogInEmailPasswordRecoverSuccessfulFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    public LogInEmailPasswordRecoverSuccessfulFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_email_password_recuperar_exitoso, container, false);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(this.getContext());
        }
        Button btnRecoverPasswordSuccessful = view.findViewById(R.id.IDButtonRecuperarPasswordExitoso);
        btnRecoverPasswordSuccessful.setOnClickListener(v -> {
            UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Login_correo", "Boton", "Olvido su clave");
            Navigation.findNavController(v).navigate(R.id.action_loginEmailPasswordRecuperarExitosoFragment_to_loginEmailFragment);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.textView);
        TextView tvLogIn = view.findViewById(R.id.IDButtonRecuperarPasswordExitoso);
        if (Query.getPortalId() == 1) {
            textView.setText(R.string.app_en_mensaje_cambio_contrasena);
            tvLogIn.setText(R.string.app_en_iniciar_sesion);
        } else {
            textView.setText(R.string.app_es_mensaje_cambio_contrasena);
            tvLogIn.setText(R.string.app_es_iniciar_sesion);
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