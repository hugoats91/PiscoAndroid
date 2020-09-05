package com.pisco.app.Utils;

import android.text.TextUtils;
import android.util.Patterns;

public class UtilText {

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
