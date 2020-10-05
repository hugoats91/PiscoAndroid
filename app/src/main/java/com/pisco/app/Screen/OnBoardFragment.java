package com.pisco.app.Screen;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pisco.app.Adapter.HorizontalMarginItemDecoration;
import com.pisco.app.Adapter.OnBoardPagerAdapter;
import com.pisco.app.Entity.ScreenItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.PiscoApplication;
import com.pisco.app.R;
import com.pisco.app.Screen.Dialogs.ProgressDialogFragment;
import com.pisco.app.Utils.UtilAnalytics;
import com.pisco.app.Utils.UtilDialog;
import com.pisco.app.Utils.ViewInstanceList;
import com.pisco.app.Utils.ViewModelInstanceList;

import java.util.ArrayList;
import java.util.List;

public class OnBoardFragment extends Fragment {
    private ViewPager2 vpOnBoard;
    OnBoardPagerAdapter adapter;
    private TabLayout tabLayout;
    private TabLayout tabLayoutBlue;
    private ConstraintLayout clContainer;
    List<ScreenItem> screenList = new ArrayList<>();
    private int vpPosition = 0;
    TextView tvSkip, tvReady;
    ImageView ivNext;
    View vLeft, vRight;
    String color;
    int dotDrawable;
    boolean whiteDot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_board, container, false);
        ViewInstanceList.setViewInstances("on-board-fragment", view);
        UtilAnalytics.sendEventScreen(PiscoApplication.getInstance(requireContext()), "Inicio onboarding");
        Context context = getContext();
        if (context != null) {
            FirebaseAnalytics.getInstance(context);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        vLeft = view.findViewById(R.id.bvLeft);
        vRight = view.findViewById(R.id.bvRight);
        clContainer = view.findViewById(R.id.clContainer);
        vpOnBoard = view.findViewById(R.id.vpOnBoard);
        tabLayoutBlue = view.findViewById(R.id.tlOnBoardBlue);
        tvSkip = view.findViewById(R.id.tvSkip);
        tvReady = view.findViewById(R.id.tvReady);
        ivNext = view.findViewById(R.id.ivNext);
        String onBoard = args.getString("type");
        setStyleText(onBoard);
        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            switch (onBoard) {
                case "onBoard-donde":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_donde_1), R.drawable.world_onboard, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_donde_2), R.drawable.onboard_map, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.onboard_donde);
                    break;
                case "onBoard-inicio":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_inicio_1), R.drawable.board_inicio_en, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_inicio_2), R.drawable.ruleta_onboard_inicio_2, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.bg_inicio);
                    break;
                case "onBoard-receta":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_receta_1), R.drawable.pisco_onboard_receta, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_receta_2), R.drawable.barman_onboard_receta, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.onboard_receta);
                    break;
                case "onBoard-resultado":
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_resultado_1), R.drawable.check_onboard_resultado_juego, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_en_onboard_resultado_2), R.drawable.happy_onboard_resultado_juego_2, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.onboard_resultado_juego);
                    break;
            }
        } else {
            switch (onBoard) {
                case "onBoard-donde":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_donde_1), R.drawable.world_onboard, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_donde_2), R.drawable.onboard_map, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.onboard_donde);
                    break;
                case "onBoard-inicio":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_inicio_1), R.drawable.board_inicio_es, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_inicio_2), R.drawable.ruleta_onboard_inicio_2, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.bg_inicio);
                    break;
                case "onBoard-receta":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_receta_1), R.drawable.pisco_onboard_receta, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_receta_2), R.drawable.barman_onboard_receta, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.onboard_receta);
                    break;
                case "onBoard-resultado":
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_resultado_1), R.drawable.check_onboard_resultado_juego, 1, onBoard, color));
                    screenList.add(new ScreenItem(getString(R.string.app_es_onboard_resultado_2), R.drawable.happy_onboard_resultado_juego_2, 2, onBoard, color));
                    clContainer.setBackgroundResource(R.drawable.onboard_resultado_juego);
                    break;
            }
        }
        adapter = new OnBoardPagerAdapter(getChildFragmentManager(), getLifecycle(), screenList);
        vpOnBoard.setAdapter(adapter);

        vpOnBoard.setOffscreenPageLimit(1);
        float nextItemVisiblePx = getResources().getDimension(R.dimen.viewpager_next_item_visible);
        float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen.viewpager_current_item_horizontal_margin);
        float pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
        vpOnBoard.setPageTransformer(((page, position) ->
        {
            page.setTranslationX(-pageTranslationX * position);
            page.setScaleY(1 - (0.25f * Math.abs(position)));
            page.setAlpha(0.25f + (1 - Math.abs(position)));
        }
        ));
        vpOnBoard.addItemDecoration(new HorizontalMarginItemDecoration(requireContext(), R.dimen.viewpager_current_item_horizontal_margin));
        tabLayout = view.findViewById(R.id.tlOnBoard);
        tabLayoutBlue = view.findViewById(R.id.tlOnBoardBlue);
        if(whiteDot){
            new TabLayoutMediator(tabLayout, vpOnBoard, ((tab, position) -> tab.setText("OBJECT " + (position + 1)))).attach();
            tabLayout.setVisibility(View.VISIBLE);
            tabLayoutBlue.setVisibility(View.GONE);
        }else{
            new TabLayoutMediator(tabLayoutBlue, vpOnBoard, ((tab, position) -> tab.setText("OBJECT " + (position + 1)))).attach();
            tabLayout.setVisibility(View.GONE);
            tabLayoutBlue.setVisibility(View.VISIBLE);
        }

        vLeft.setOnClickListener(v -> {
            String[] response = getOnBoard(onBoard, vpPosition);
            ProgressDialogFragment progress = UtilDialog.showProgress(this);
            ViewModelInstanceList.getWelcomeViewModelInstance().updateReadyOnboardingFront(response[0], response[1], () -> {
                progress.dismiss();
                nextFragment(view, onBoard);
            });
        });
        vRight.setOnClickListener(v -> {
            if (vpPosition == 0) {
                UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Inicio_onboarding", "Boton", "Flecha_siguiente");
                vpOnBoard.setCurrentItem(1, true);
            } else {
                UtilAnalytics.sendEvent(PiscoApplication.getInstance(requireContext()), "send", "event", "Inicio_onboarding", "Boton", "Listo");
                String[] response = getOnBoard(onBoard, vpPosition);
                ProgressDialogFragment progress = UtilDialog.showProgress(this);
                ViewModelInstanceList.getWelcomeViewModelInstance().updateReadyOnboardingFront(response[0], response[1], () -> {
                    progress.dismiss();
                    nextFragment(view, onBoard);
                });
            }

        });
        vpOnBoard.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {
                vpPosition = position;
                if (position == 0) {
                    tvSkip.setVisibility(View.VISIBLE);
                    tvReady.setVisibility(View.GONE);
                    ivNext.setVisibility(View.VISIBLE);
                } else {
                    tvSkip.setVisibility(View.GONE);
                    tvReady.setVisibility(View.VISIBLE);
                    ivNext.setVisibility(View.GONE);
                }
            }
        });
    }

    public void nextFragment(View view, String onBoard) {
        switch (onBoard) {
            case "onBoard-donde":
                Navigation.findNavController(view).navigate(OnBoardFragmentDirections.actionOnBoardFragmentToDondeFragment());
                break;
            case "onBoard-inicio":
                Navigation.findNavController(view).navigate(OnBoardFragmentDirections.actionOnBoardFragmentToInicioFragment());
                break;
            case "onBoard-receta":
                Navigation.findNavController(view).navigate(OnBoardFragmentDirections.actionOnBoardFragmentToRecetaFragment());
                break;
            case "onBoard-resultado":
                Navigation.findNavController(view).navigate(OnBoardFragmentDirections.actionOnBoardFragmentToResultadoFragment());
                break;
        }
    }

    public void setStyleText(String onBoard) {
        color = "#ffffff";
        dotDrawable = 0;
        int nextDrawable = 0;
        switch (onBoard) {
            case "onBoard-donde":
            case "onBoard-receta":
            case "onBoard-resultado":
                color = "#ffffff";
                nextDrawable = R.drawable.next_white;
                dotDrawable = R.drawable.tab_selector_white;
                whiteDot = true;
                break;
            case "onBoard-inicio":
                color = "#313131";
                nextDrawable = R.drawable.imagensiguienteblack;
                dotDrawable = R.drawable.tab_selector_blue;
                whiteDot = false;
                break;
        }
        tvSkip.setTextColor(Color.parseColor(color));
        tvReady.setTextColor(Color.parseColor(color));
        ivNext.setImageResource(nextDrawable);
    }

    public String[] getOnBoard(String onBoard, int position) {
        String onboardAdapter = "";
        String pageNumber = "";
        String[] response = new String[2];
        if (onBoard.equals("onBoard-inicio") && position == 0) {
            onboardAdapter = "OnBoard 1.1";
            pageNumber = "OnBoard 1.1";
        } else if (onBoard.equals("onBoard-inicio") && position == 1) {
            onboardAdapter = "OnBoard 1.2";
            pageNumber = "OnBoard 1.2";
        } else if (onBoard.equals("onBoard-receta") && position == 0) {
            onboardAdapter = "OnBoard 2.1";
            pageNumber = "OnBoard 2.1";
        } else if (onBoard.equals("onBoard-receta") && position == 1) {
            onboardAdapter = "OnBoard 2.2";
            pageNumber = "OnBoard 2.2";
        } else if (onBoard.equals("onBoard-donde") && position == 0) {
            onboardAdapter = "OnBoard 3.1";
            pageNumber = "OnBoard 3.1";
        } else if (onBoard.equals("onBoard-donde") && position == 1) {
            onboardAdapter = "OnBoard 3.2";
            pageNumber = "OnBoard 3.2";
        } else if (onBoard.equals("onBoard-resultado") && position == 0) {
            onboardAdapter = "OnBoard 4.1";
            pageNumber = "OnBoard 4.1";
        } else if (onBoard.equals("onBoard-resultado") && position == 1) {
            onboardAdapter = "OnBoard 4.2";
            pageNumber = "OnBoard 4.2";
        }
        response[0] = onboardAdapter;
        response[1] = pageNumber;
        return response;
    }

}