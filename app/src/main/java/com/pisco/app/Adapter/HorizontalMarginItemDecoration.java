package com.pisco.app.Adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration {

    private int horizontalMarginInPx;

    public HorizontalMarginItemDecoration(Context context, @DimenRes int horizontalMarginInDp) {
        this.horizontalMarginInPx = (int)context.getResources().getDimension(horizontalMarginInDp);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = horizontalMarginInPx;
        outRect.left = horizontalMarginInPx;
    }

}