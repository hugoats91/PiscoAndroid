package com.pisco.app.Utils;

import com.pisco.app.ViewModel.WelcomeViewModel;
import com.pisco.app.ViewModel.HomeViewModel;
import com.pisco.app.ViewModel.LogInEmailRegisterViewModel;
import com.pisco.app.ViewModel.LoginViewModel;

public class ViewModelInstanceList {

    private static final LogInEmailRegisterViewModel logInEmailRegisterViewModel = new LogInEmailRegisterViewModel();
    private static final LoginViewModel loginViewModel = new LoginViewModel();
    private static final WelcomeViewModel welcomeViewModel = new WelcomeViewModel();
    private static final HomeViewModel homeViewModel = new HomeViewModel();

    public static LogInEmailRegisterViewModel getLogInEmailRegisterViewModelInstance() {
        return logInEmailRegisterViewModel;
    }

    public static LoginViewModel getLogInViewModelInstance() {
        return loginViewModel;
    }

    public static WelcomeViewModel getWelcomeViewModelInstance() {
        return welcomeViewModel;
    }

    public static HomeViewModel getHomeViewModelInstance() {
        return homeViewModel;
    }

}