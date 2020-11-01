package com.promperu.pisco.Adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.promperu.pisco.Entity.PromotionPiscoItem;
import com.promperu.pisco.Screen.PromotionPiscoFragment;

import java.util.List;

public class PromotionPiscoViewPagerAdapter extends FragmentStatePagerAdapter {

    Context mContext ;
    private List<PromotionPiscoItem> listScreen;

    public PromotionPiscoViewPagerAdapter(Context context, List<PromotionPiscoItem> listScreen, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.listScreen = listScreen;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PromotionPiscoFragment();
        Bundle args = new Bundle();
        args.putInt(PromotionPiscoFragment.ARG_OBJECT, position);
        args.putString(PromotionPiscoFragment.PromTitulo, this.listScreen.get(position).getPromTitle());
        args.putString(PromotionPiscoFragment.PromPromocion, this.listScreen.get(position).getPromPromotion());
        args.putString(PromotionPiscoFragment.PromImagen, this.listScreen.get(position).getPromImage());
        args.putString(PromotionPiscoFragment.PromSubTitulo, this.listScreen.get(position).getPromSubTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public float getPageWidth(int position) {
        float pagesCount = (float) 2.5; // You could display partial pages using a float value
        return (1 / pagesCount);
    }

    @Override
    public int getCount() { return listScreen.size(); }

    @Override
    public CharSequence getPageTitle(int position) { return "OBJECT " + (position + 1); }

}

