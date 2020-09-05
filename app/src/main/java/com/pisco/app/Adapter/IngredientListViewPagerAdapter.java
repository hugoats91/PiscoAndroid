package com.pisco.app.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pisco.app.Entity.IngredientItem;
import com.pisco.app.Screen.IngredientObjectFragment;

import java.util.List;

public class IngredientListViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<IngredientItem> listScreen;

    @SuppressWarnings("deprecation")
    public IngredientListViewPagerAdapter(List<IngredientItem> listScreen, FragmentManager fm) {
        super(fm);
        this.listScreen = listScreen;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new IngredientObjectFragment();
        Bundle args = new Bundle();
        args.putInt(IngredientObjectFragment.ARG_OBJECT, position);
        args.putString(IngredientObjectFragment.IngrId, String.valueOf(this.listScreen.get(position).getIngrId()));
        args.putString(IngredientObjectFragment.IngrTitulo, this.listScreen.get(position).getIngrTitle());
        args.putString(IngredientObjectFragment.IngrIcono, this.listScreen.get(position).getIngrIcon());
        args.putString(IngredientObjectFragment.IngrTipoDescripcion, this.listScreen.get(position).getIngrDescription());
        args.putString(IngredientObjectFragment.IngrDescripcion, this.listScreen.get(position).getIngrDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public float getPageWidth(int position) {
        float pagesCount = (float) 5.5; // You could display partial pages using a float value
        return (1 / pagesCount);
    }

    @Override
    public int getCount() { return listScreen.size(); }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

}