package com.pisco.app.Utils;

import android.content.Context;

import com.pisco.app.R;

public class UtilText {

    public static String capitalize(String text) {
        if(text.isEmpty()){
            return text;
        }else{
            StringBuilder sb = new StringBuilder(text);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
    }

    public static String errorRegister(int type, Context context) {
        String error = "";
        switch (type) {
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
