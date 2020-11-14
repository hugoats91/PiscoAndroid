package com.promperu.pisco.Screen.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.UtilUser;

public class NoConnectionDialogFragment extends DialogFragment {

    public NoConnectionDialogFragment() {}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_network, null);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        if (UtilUser.getUser().getPortalId() == 0) {
            tvTitle.setText(getString(R.string.app_es_no_conexion_titulo));
            tvDescription.setText(getString(R.string.app_es_no_conexion));
        }else{
            tvTitle.setText(getString(R.string.app_en_no_conexion_titulo));
            tvDescription.setText(getString(R.string.app_en_no_conexion));
        }
    }
}