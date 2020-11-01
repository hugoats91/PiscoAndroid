package com.promperu.pisco;

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

import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.Utils.ViewModelInstanceList;
import com.promperu.pisco.Screen.Dialogs.NoConnectionDialogFragment;
import com.promperu.pisco.Screen.WelcomeHomeFragment;
import com.promperu.pisco.Screen.StartFragment;
import com.promperu.pisco.Screen.LogInEmailFragment;
import com.promperu.pisco.Screen.LogInEmailPasswordRecoverSuccessfulFragment;
import com.promperu.pisco.Screen.LogInEmailPasswordRecoverFragment;
import com.promperu.pisco.Screen.LogInEmailRegisterFragment;
import com.promperu.pisco.Screen.LogInFragment;
import com.promperu.pisco.Screen.WhereBuyFragment;
import com.promperu.pisco.Screen.InitialRecipeFragment;
import com.promperu.pisco.Screen.ProfileFragment;
import com.promperu.pisco.Screen.RecipeFragment;
import com.promperu.pisco.Screen.AskResultFragment;
import com.promperu.pisco.Screen.ResultGameFragment;
import com.promperu.pisco.Screen.ResultPlayAgainFragment;

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