package com.pisco.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.pisco.app.Entity.ScreenItem;
import com.pisco.app.LocalService.AppDatabase;
import com.pisco.app.R;
import com.pisco.app.Utils.ViewModelInstanceList;
import com.pisco.app.ViewModel.WelcomeViewModel;

import java.util.List;

public class BoardViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<ScreenItem> listScreen;
    private OnboardingListener listener;

    public BoardViewPagerAdapter(Context context, List<ScreenItem> listScreen, OnboardingListener listener) {
        this.context = context;
        this.listScreen = listScreen;
        this.listener = listener;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        String onboard = listScreen.get(position).getOnboard();
        View layoutScreen;
        if (onboard.equals("onboard-inicio") && position == 0) {
            layoutScreen = inflater.inflate(R.layout.layout_background_pp_onboard_inicio,null);
        } else {
            layoutScreen = inflater.inflate(R.layout.layout_background_pp_onboard,null);
        }
        Button btn1 = layoutScreen.findViewById(R.id.IDTexViewTab1);
        Button btn2 = layoutScreen.findViewById(R.id.IDTexViewTab2);
        TextView tvReady = layoutScreen.findViewById(R.id.IDTexViewListo);
        ImageView ivNext = layoutScreen.findViewById(R.id.IDImageViewNext);
        TextView tvSkip = layoutScreen.findViewById(R.id.IDTexViewSaltar);

        if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1) {
            tvReady.setText(R.string.app_en_listo);
            tvSkip.setText(R.string.app_en_saltar);
        } else {
            tvReady.setText(R.string.app_es_listo);
            tvSkip.setText(R.string.app_es_saltar);
        }
        String color="#ffffff";
        if (onboard.equals("onboard-donde") && position == 0) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_donde);
            btn1.setBackgroundResource(R.drawable.circle_solid_transparent);
            btn2.setBackgroundResource(R.drawable.circle_no_solid_transparent);
            ivNext.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.VISIBLE);
            tvReady.setVisibility(View.GONE);
            ivNext.setBackgroundResource(R.drawable.next_white);
        } else if(onboard.equals("onboard-donde") && position == 1) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_donde);
            btn1.setBackgroundResource(R.drawable.circle_no_solid_transparent);
            btn2.setBackgroundResource(R.drawable.circle_solid_transparent);
            ivNext.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
            tvReady.setVisibility(View.VISIBLE);
        } else if(onboard.equals("onboard-inicio") && position == 0) {
            if (AppDatabase.INSTANCE.userDao().getEntityUser().getPortalId() == 1 ){
                layoutScreen.setBackgroundResource(R.drawable.onboard_inicio_hola);
            }else{
                layoutScreen.setBackgroundResource(R.drawable.onboard_inicio_hello);
            }
            btn1.setBackgroundResource(R.drawable.circle_solid);
            btn2.setBackgroundResource(R.drawable.circle_no_solid);
            ivNext.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.VISIBLE);
            tvReady.setVisibility(View.GONE);
            ivNext.setBackgroundResource(R.drawable.imagensiguienteblack);
            color = "#313131";
        } else if(onboard.equals("onboard-inicio") && position == 1) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_inicio_2);
            btn1.setBackgroundResource(R.drawable.circle_no_solid);
            btn2.setBackgroundResource(R.drawable.circle_solid);
            ivNext.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
            tvReady.setVisibility(View.VISIBLE);
            color = "#313131";
        } else if(onboard.equals("onboard-receta") && position == 0) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_receta);
            btn1.setBackgroundResource(R.drawable.circle_solid_transparent);
            btn2.setBackgroundResource(R.drawable.circle_no_solid_transparent);
            ivNext.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.VISIBLE);
            tvReady.setVisibility(View.GONE);
            ivNext.setBackgroundResource(R.drawable.next_white);
        } else if(onboard.equals("onboard-receta") && position == 1) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_receta);
            btn1.setBackgroundResource(R.drawable.circle_no_solid_transparent);
            btn2.setBackgroundResource(R.drawable.circle_solid_transparent);
            ivNext.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
            tvReady.setVisibility(View.VISIBLE);
        } else if(onboard.equals("onboard-resultado") && position == 0) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_resultado_juego);
            btn1.setBackgroundResource(R.drawable.circle_solid_transparent);
            btn2.setBackgroundResource(R.drawable.circle_no_solid_transparent);
            ivNext.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.VISIBLE);
            tvReady.setVisibility(View.GONE);
            ivNext.setBackgroundResource(R.drawable.next_white);
       } else if(onboard.equals("onboard-resultado") && position == 1) {
            layoutScreen.setBackgroundResource(R.drawable.onboard_resultado_juego_2);
            btn1.setBackgroundResource(R.drawable.circle_no_solid_transparent);
            btn2.setBackgroundResource(R.drawable.circle_solid_transparent);
            ivNext.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
            tvReady.setVisibility(View.VISIBLE);
        }
        ImageView ivSlide = layoutScreen.findViewById(R.id.IDImageCenterOnBoard);
        TextView tvDescription = layoutScreen.findViewById(R.id.IDTexViewDescription);
        tvDescription.setTextColor(Color.parseColor(color));
        tvDescription.setText(Html.fromHtml(listScreen.get(position).getDescription()));

        tvSkip.setTextColor(Color.parseColor(color));
        tvReady.setTextColor(Color.parseColor(color));

        TextView tvNumbers = layoutScreen.findViewById(R.id.IDTexViewDescriptionNumbers);
        tvNumbers.setText(Html.fromHtml("<sup><b>"+ listScreen.get(position).getPosition()+"</b>/<small>2</small></sup>"));
        tvNumbers.setTextColor(Color.parseColor(color));

        tvSkip.setOnClickListener(v -> {
            String[] response = getOnBoard(onboard, position);
            ViewModelInstanceList.getWelcomeViewModelInstance().updateReadyOnboardingFront(response[0], response[1], () -> {
                this.listener.close(response);
            });
        });

        ivSlide.setImageResource(listScreen.get(position).getScreenImg());

        tvReady.setOnClickListener(v -> {
            String[] response = getOnBoard(onboard, position);
            ViewModelInstanceList.getWelcomeViewModelInstance().updateReadyOnboardingFront(response[0], response[1], () -> {
                this.listener.close(response);
            });
        });

        ivNext.setOnClickListener(v -> listener.nextPage());
        container.addView(layoutScreen);
        return layoutScreen;
    }

    public String[] getOnBoard(String onboard,int position){
        String onboardAdapter = "";
        String pageNumber = "";
        String[] response = new String[2];
        if (onboard.equals("onboard-inicio") && position == 0) {
            onboardAdapter = "OnBoard 1.1";
            pageNumber = "OnBoard 1.1";
        } else if(onboard.equals("onboard-inicio") && position == 1) {
            onboardAdapter = "OnBoard 1.2";
            pageNumber = "OnBoard 1.2";
        } else if(onboard.equals("onboard-receta") && position == 0) {
            onboardAdapter = "OnBoard 2.1";
            pageNumber = "OnBoard 2.1";
        } else if(onboard.equals("onboard-receta") && position == 1) {
            onboardAdapter = "OnBoard 2.2";
            pageNumber = "OnBoard 2.2";
        } else if(onboard.equals("onboard-donde") && position == 0) {
            onboardAdapter = "OnBoard 3.1";
            pageNumber = "OnBoard 3.1";
        } else if(onboard.equals("onboard-donde") && position == 1) {
            onboardAdapter = "OnBoard 3.2";
            pageNumber = "OnBoard 3.2";
        } else if(onboard.equals("onboard-resultado") && position == 0) {
            onboardAdapter = "OnBoard 4.1";
            pageNumber = "OnBoard 4.1";
        } else if(onboard.equals("onboard-resultado") && position == 1) {
            onboardAdapter = "OnBoard 4.2";
            pageNumber = "OnBoard 4.2";
        }
        response[0] = onboardAdapter;
        response[1] = pageNumber;
        return response;
    }

    @Override
    public int getCount() {
        return listScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public interface OnboardingListener {

        void nextPage();

        void close(String[] response);

    }

}