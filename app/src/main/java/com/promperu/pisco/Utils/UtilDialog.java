package com.promperu.pisco.Utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.promperu.pisco.Screen.Dialogs.ProgressDialogFragment;

public class UtilDialog {

    public interface InfoMessageCallback{
        void onAccept();
    }

    public interface InfoCancelMessageCallback{
        void onCancel();
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

    public static void infoTwoOptionsMessage(Context context, String title, String message, InfoMessageCallback listener, InfoCancelMessageCallback cancelListener){
        String cancel = "";
        if(Query.getPortalId()==1){
            cancel = "Cancel";
        }else{
            cancel = "Cancelar";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", (DialogInterface dialog, int which) -> {
            dialog.dismiss();
            listener.onAccept();
        }).setNegativeButton(cancel, (DialogInterface dialog, int which) ->{
            dialog.dismiss();
            cancelListener.onCancel();
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