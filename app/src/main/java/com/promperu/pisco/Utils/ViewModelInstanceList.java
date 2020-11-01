package com.promperu.pisco.Utils;

import com.promperu.pisco.ViewModel.WelcomeViewModel;
import com.promperu.pisco.ViewModel.HomeViewModel;
import com.promperu.pisco.ViewModel.LogInEmailRegisterViewModel;
import com.promperu.pisco.ViewModel.LoginViewModel;

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