package com.promperu.pisco.Screen.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.promperu.pisco.Adapter.BoardViewPagerAdapter;
import com.promperu.pisco.Entity.ScreenItem;
import com.promperu.pisco.LocalService.AppDatabase;
import com.promperu.pisco.R;
import com.promperu.pisco.Utils.FragmentInstanceList;
import com.promperu.pisco.Utils.ViewInstanceList;

import java.util.ArrayList;
import java.util.List;

public class OnboardDialogFragment extends DialogFragment implements BoardViewPagerAdapter.OnboardingListener {

    private ViewPager screenPager;
    private OnBoardDialogListener listener;
    private String[] response;

    public OnboardDialogFragment(OnBoardDialogListener listener) {
        this.listener = listener;
    }

    public interface OnBoardDialogListener{
        void close(String[] response);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_onboard, null);
        Fragment fragment = getParentFragment();
        FragmentInstanceList.setFragmentInstances("onboard-fragment",fragment);
        builder.setView(view);
        ViewInstanceList.setViewInstances("onboard-fragment",view);
        AlertDialog alertDialog = builder.create();
        FragmentInstanceList.setDictionaryAlertDialogInstances("onboard-fragment-dialog",alertDialog);
        List<ScreenItem> screenList = new ArrayList<>();
        String onboard = "";
        Bundle args = getArguments();
        if (args != null) {
            onboard = args.getString("onboard", "");
        }
        screenList.clear();
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            switch (onboard) {
                case "onboard-donde":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_donde_1), R.drawable.world_onboard, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_donde_2), R.drawable.onboard_map, 2, onboard));
                    break;
                case "onboard-inicio":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_inicio_1), R.drawable.onboard_inicio_hola, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_inicio_2), R.drawable.ruleta_onboard_inicio_2, 2, onboard));
                    break;
                case "onboard-receta":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_receta_1), R.drawable.pisco_onboard_receta, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_receta_2), R.drawable.barman_onboard_receta, 2, onboard));
                    break;
                case "onboard-resultado":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_resultado_1), R.drawable.check_onboard_resultado_juego, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_resultado_2), R.drawable.happy_onboard_resultado_juego_2, 2, onboard));
                    break;
            }
        } else {
            switch (onboard) {
                case "onboard-donde":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_donde_1), R.drawable.world_onboard, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_donde_2), R.drawable.onboard_map, 2, onboard));
                    break;
                case "onboard-inicio":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_inicio_1), R.drawable.onboard_inicio_hello, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_inicio_2), R.drawable.ruleta_onboard_inicio_2, 2, onboard));
                    break;
                case "onboard-receta":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_receta_1), R.drawable.pisco_onboard_receta, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_receta_2), R.drawable.barman_onboard_receta, 2, onboard));
                    break;
                case "onboard-resultado":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_resultado_1), R.drawable.check_onboard_resultado_juego, 1, onboard));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_resultado_2), R.drawable.happy_onboard_resultado_juego_2, 2, onboard));
                    break;
            }
        }
        TabLayout tabIndicator = view.findViewById(R.id.tab_indicator);
        screenPager = view.findViewById(R.id.screen_viewpager);
        BoardViewPagerAdapter boardViewPagerAdapter = new BoardViewPagerAdapter(view.getContext(), screenList, this);
        screenPager.setAdapter(boardViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);
        return alertDialog;
    }

    @Override
    public void nextPage() {
        screenPager.setCurrentItem(1);
    }

    @Override
    public void close(String[] response) {
        this.response = response;
        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.close(response);
    }
}