package com.pisco.app.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.pisco.app.R;

public class UtilText {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String errorRegister(int type, Context context){
        String error = "";
        switch (type){
            case 1:
                if (Query.getPortalId() == 1) {
                    error = context.getString(R.string.app_en_login_clasico_error);
                } else {
                    error = context.getString(R.string.app_es_login_clasico_error);
                }
                break;
            case 5:
                if (Query.getPortalId() == 1) {
                    error = context.getString(R.string.app_en_facebook_error);
                } else {
                    error = context.getString(R.string.app_es_facebook_error);
                }
                break;
            case 6:
                if (Query.getPortalId() == 1) {
                    error = context.getString(R.string.app_en_google_error);
                } else {
                    error = context.getString(R.string.app_es_google_error);
                }
                break;
        }
        return error;
    }
}
