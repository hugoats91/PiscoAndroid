package com.pisco.app;

import android.app.Application;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PiscoApplication extends Application {
    public FirebaseAnalytics firebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public static PiscoApplication getInstance(Context context) {
        return ((PiscoApplication) context.getApplicationContext());
    }
}
