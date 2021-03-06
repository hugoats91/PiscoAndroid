package com.promperu.pisco.Screen.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.UtilUser;

public class TrophyDialogFragment extends DialogFragment {

    public interface Listener{
        void onClose();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    Listener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_pp_in_ruleta_ok, null);
        TextView tvCongratulations = view.findViewById(R.id.IDTextViewFelicitacion);
        TextView tvSubtitle = view.findViewById(R.id.tvSubtitle);
        if (UtilUser.getUser().getPortalId() == 1) {
            tvCongratulations.setText(getString(R.string.app_en_trophy_congratulations));
            tvSubtitle.setText(getString(R.string.app_en_trophy_subtitle));
        } else {
            tvCongratulations.setText(getString(R.string.app_es_trophy_congratulations));
            tvSubtitle.setText(getString(R.string.app_es_trophy_subtitle));
        }
        ImageView ivClose = view.findViewById(R.id.ImageViewButtonMenuDialogRuletaClose);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        ivClose.setOnClickListener(v -> {
            alertDialog.dismiss();
            if(listener!=null){
                listener.onClose();
            }else{
                requireActivity().onBackPressed();
            }
        });
        return alertDialog;
    }

}