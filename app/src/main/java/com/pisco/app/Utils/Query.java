package com.pisco.app.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Patterns;

import com.facebook.login.widget.LoginButton;
import com.facebook.share.Share;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.pisco.app.Enum.CountryPortalId;

import java.util.Locale;
import java.util.regex.Pattern;

public class Query {

    public static String language = Locale.getDefault().getLanguage();
    @SuppressLint("StaticFieldLeak")
    public static GoogleSignInClient googleSignInClient;
    public static LoginButton btnFacebook;
    public static String PREFS_KEY = "mispreferencias";

    public static int getPortalId() {
        if(Query.language.equals("es")||
                Query.language.equals("es-AR")||
                Query.language.equals("es-BO")||
                Query.language.equals("es-CL")||
                Query.language.equals("es-CO")||
                Query.language.equals("es-CR")||
                Query.language.equals("es-DO")||
                Query.language.equals("es-EC")||
                Query.language.equals("es-ES")||
                Query.language.equals("es-GT")||
                Query.language.equals("es-HN")||
                Query.language.equals("es-MX")||
                Query.language.equals("es-NI")||
                Query.language.equals("es-PA")||
                Query.language.equals("es-PE")||
                Query.language.equals("es-PR")||
                Query.language.equals("es-PY")||
                Query.language.equals("es-SV")||
                Query.language.equals("es-UY")||
                Query.language.equals("es-VE")){
            return CountryPortalId.ES.ordinal();
        } else {
            return CountryPortalId.OTHERS.ordinal();
        }
    }

    public static String getUserCountry(Context context) {
        try {
            String respuesta = null;
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                return null;
            }
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                respuesta= simCountry.toUpperCase();
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    respuesta= networkCountry.toUpperCase();
                }
            }
            return respuesta;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static void saveValue(String correo, String password, Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString("correo", correo);
        editor.putString("password", password);
        editor.apply();
    }

    public static void saveCityCount(Context context, int count){
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putInt("count", count);
        editor.apply();
    }

    public static void saveLoginType(Context context, int type){
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putInt("login_type", type);
        editor.apply();
    }

    public static void clearValuePreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.remove("correo");
        editor.remove("password");
        editor.apply();
    }

    public static String readValue(String keyPref, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        if(preferences.contains(keyPref)){
            return preferences.getString(keyPref, "");
        }else{
            return "";
        }
    }

    public static int readValueInt(String keyPref, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        if(preferences.contains(keyPref)){
            return preferences.getInt(keyPref, 0);
        }else{
            return 0;
        }
    }

}
