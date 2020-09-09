package com.pisco.app.Utils;

import android.os.Bundle;

import com.pisco.app.PiscoApplication;

public class UtilAnalytics {

    public static void sendEvent(PiscoApplication application, String event, String hitType, String eventCategory, String eventAction, String eventLabel) {
        Bundle bundle = new Bundle();
        bundle.putString("hitType", hitType);
        bundle.putString("eventCategory", eventCategory);
        bundle.putString("eventAction", eventAction);
        bundle.putString("eventLabel", eventLabel);
        application.firebaseAnalytics.logEvent(event, bundle);
    }
}