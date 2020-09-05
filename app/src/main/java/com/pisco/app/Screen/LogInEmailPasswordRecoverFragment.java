package com.pisco.app.Screen;

import android.annotation.SuppressLint;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pisco.app.R;
import com.pisco.app.Utils.Query;
import com.pisco.app.Utils.UtilDialog;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Utils.ViewInstanceList;

import org.jetbrains.annotations.NotNull;

public class LogInEmailPasswordRecoverFragment extends Fragment {

    private OnFragmentInteractionListener listener;

    public LogInEmailPasswordRecoverFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_email_password_recuperar, container, false);
        ViewInstanceList.setViewInstances("login-email-password-recuperar-fragment",view);
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        return view;
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

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnRecoverPassword = view.findViewById(R.id.IDButtonRecuperarPassword);
        TextView tvPassword = view.findViewById(R.id.textView);
        TextView tvEmail = view.findViewById(R.id.IdEditTextLoginEmailResetCorreo);
        Button btnRecover = view.findViewById(R.id.IDButtonRecuperarPassword);
        if (Query.getPortalId() == 1) {
            tvPassword.setText(R.string.app_en_recuperar_contrasena);
            tvEmail.setHint(R.string.app_en_correo);
            btnRecover.setText(R.string.app_en_recuperar);
        } else {
            tvPassword.setText(R.string.app_es_recuperar_contrasena);
            tvEmail.setHint(R.string.app_es_correo);
            btnRecover.setText(R.string.app_es_recuperar);
        }
        btnRecoverPassword.setOnClickListener(v -> {
            String email = tvEmail.getText().toString();
            if (Query.validateEmail(email)){
                tvEmail.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                ViewModelInstanceList.getLogInViewModelInstance().postRecoverPasswordFront(LogInEmailPasswordRecoverFragment.this, email);
            }else{
                if (Query.getPortalId() == 1) {
                    UtilDialog.infoMessage(getContext(), getString(R.string.app_name), getString(R.string.app_en_formato_email));
                }else{
                    UtilDialog.infoMessage(getContext(), getString(R.string.app_name), getString(R.string.app_es_formato_email));
                }

                tvEmail.setCompoundDrawablesWithIntrinsicBounds(null,null, AppCompatResources.getDrawable(view.getContext(),R.drawable.alertasesion),null);
                new android.os.Handler().postDelayed(() -> tvEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null), 2500);
            }
        });
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