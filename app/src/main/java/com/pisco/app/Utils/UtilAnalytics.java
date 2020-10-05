package com.pisco.app.Utils;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pisco.app.PiscoApplication;

import timber.log.Timber;

public class UtilAnalytics {

    public static void sendEvent(PiscoApplication application, String event, String hitType, String eventCategory, String eventAction, String eventLabel) {
        Timber.i(event + " - "+ hitType+" - "+eventCategory+" - "+eventAction);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventLabel);
        application.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void sendEventScreen(PiscoApplication application, String eventLabel) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, eventLabel);
        application.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}
