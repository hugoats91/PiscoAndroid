package com.pisco.app.Utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.pisco.app.Screen.Dialogs.ProgressDialogFragment;

public class UtilDialog {

    public interface InfoMessageCallback{
        void onAccept();
    }

    public static void infoMessage(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", (DialogInterface dialog, int which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void infoMessage(Context context, String title, String message, InfoMessageCallback listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", (DialogInterface dialog, int which) -> {
            dialog.dismiss();
            listener.onAccept();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static ProgressDialogFragment showProgress(Fragment fragment){
        ProgressDialogFragment frag = new ProgressDialogFragment();
        frag.show(fragment.getChildFragmentManager(), "");
        return frag;
    }

}