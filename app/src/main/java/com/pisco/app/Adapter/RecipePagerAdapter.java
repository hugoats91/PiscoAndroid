package com.pisco.app.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pisco.app.Entity.RecipeItem;
import com.pisco.app.Screen.RecipeObjectFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipePagerAdapter extends FragmentStateAdapter {

    private List<RecipeItem> recipeList;
    private ArrayList<Long> pageIds = new ArrayList<>();

    public RecipePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<RecipeItem> recipeList) {
        super(fragmentManager, lifecycle);
        this.recipeList = recipeList;
        for(RecipeItem recipe: recipeList){
            pageIds.add((long)recipe.hashCode());
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new RecipeObjectFragment();
        Bundle args = new Bundle();
        args.putInt(RecipeObjectFragment.ARG_OBJECT, position);
        args.putString(RecipeObjectFragment.ReceId, String.valueOf(this.recipeList.get(position).getRecipeId()));
        args.putString(RecipeObjectFragment.ReceTitulo, this.recipeList.get(position).getTitle());
        args.putString(RecipeObjectFragment.RecePreparacion, this.recipeList.get(position).getPreparation());
        args.putString(RecipeObjectFragment.ReceDescripcion, this.recipeList.get(position).getDescription());
        args.putString(RecipeObjectFragment.ReceImagenReceta, this.recipeList.get(position).getImage());
        args.putString(RecipeObjectFragment.ReceImagenRecetaCabecera, this.recipeList.get(position).getImageHeader());
        args.putString(RecipeObjectFragment.ReceUsuarioMeGusta, String.valueOf(this.recipeList.get(position).getUserLike()));
        args.putString(RecipeObjectFragment.ReceUsuarioId, String.valueOf(this.recipeList.get(position).getUserId()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public long getItemId(int position) {
        return recipeList.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return pageIds.contains(itemId);
    }
}
