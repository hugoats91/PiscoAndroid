package com.promperu.pisco.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.promperu.pisco.Entity.RecipeItem;
import com.promperu.pisco.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeBlockAdapter extends RecyclerView.Adapter<RecipeBlockAdapter.ViewHolder> {

    private List<RecipeItem> listReceipt;

    public RecipeBlockAdapter(List<RecipeItem> listReceipt) {
        this.listReceipt = listReceipt;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta_block, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = listReceipt.get(position).getImage();
        Picasso.get().load(url).into(holder.ivRecipe);
    }

    @Override
    public int getItemCount() {
        return listReceipt.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivRecipe;

        public ViewHolder(View v) {
            super(v);
            ivRecipe = v.findViewById(R.id.IDRecetaBlockImagen);
        }

    }

}