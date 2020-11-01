package com.promperu.pisco.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.promperu.pisco.Entity.IngredientItem;
import com.promperu.pisco.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<IngredientItem> listScreen;

    public IngredientsAdapter(List<IngredientItem> listScreen) {
        this.listScreen = listScreen;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingrediente, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(listScreen.get(position).getIngrTitle());
        String url = listScreen.get(position).getIngrIcon();
        Picasso.get().load(url).into(holder.ivIngredient);
    }

    @Override
    public int getItemCount() {
        return listScreen.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIngredient;
        private TextView tvTitle;

        public ViewHolder(View v) {
            super(v);
            ivIngredient = v.findViewById(R.id.IDIngrIcono);
            tvTitle = v.findViewById(R.id.IDIngrTitulo);
        }

    }

}