package com.pisco.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.Screen.Dialogs.NoConnectionDialogFragment;
import com.pisco.app.Screen.WelcomeHomeFragment;
import com.pisco.app.Screen.StartFragment;
import com.pisco.app.Screen.LogInEmailFragment;
import com.pisco.app.Screen.LogInEmailPasswordRecoverSuccessfulFragment;
import com.pisco.app.Screen.LogInEmailPasswordRecoverFragment;
import com.pisco.app.Screen.LogInEmailRegisterFragment;
import com.pisco.app.Screen.LogInFragment;
import com.pisco.app.Screen.WhereBuyFragment;
import com.pisco.app.Screen.InitialRecipeFragment;
import com.pisco.app.Screen.ProfileFragment;
import com.pisco.app.Screen.RecipeFragment;
import com.pisco.app.Screen.AskResultFragment;
import com.pisco.app.Screen.ResultGameFragment;
import com.pisco.app.Screen.ResultPlayAgainFragment;

public class SplashActivity extends AppCompatActivity implements
        LogInFragment.OnFragmentInteractionListener,
        LogInEmailFragment.OnFragmentInteractionListener,
        LogInEmailPasswordRecoverFragment.OnFragmentInteractionListener,
        LogInEmailPasswordRecoverSuccessfulFragment.OnFragmentInteractionListener,
        LogInEmailRegisterFragment.OnFragmentInteractionListener,
        StartFragment.OnFragmentInteractionListener,
        WelcomeHomeFragment.OnFragmentInteractionListener,
        WhereBuyFragment.OnFragmentInteractionListener,
        InitialRecipeFragment.OnFragmentInteractionListener,
        ResultGameFragment.OnFragmentInteractionListener,
        ResultPlayAgainFragment.OnFragmentInteractionListener,
        AskResultFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        RecipeFragment.OnFragmentInteractionListener{

    NoConnectionDialogFragment dialog = new NoConnectionDialogFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_relative);
        AppDatabase.getAppDatabase(this);
        int DURACION_SPLASH = 4000;
        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_pp_login);
            ViewModelInstanceList.getLogInEmailRegisterViewModelInstance().countryListFront(getApplicationContext());
        }, DURACION_SPLASH);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isWifiConn = false;
            boolean isMobileConn = false;
            if (connMgr != null) {
                for (Network network : connMgr.getAllNetworks()) {
                    NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                    if (networkInfo == null) {
                        continue;
                    }
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isWifiConn |= networkInfo.isConnected();
                    }
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isMobileConn |= networkInfo.isConnected();
                    }
                }
            }
            if (isWifiConn || isMobileConn) {
                dismissDialog();
            } else {
                dialog.show(getSupportFragmentManager(), "no_internet");
            }
        }
    };

    public void dismissDialog(){
        Fragment prev = getSupportFragmentManager().findFragmentByTag("no_internet");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

}