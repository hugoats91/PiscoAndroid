package com.promperu.pisco.Entity;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CarouselLinearLayout extends LinearLayout {

    public CarouselLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CarouselLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = this.getWidth();
        float h = this.getHeight();
        float scale = 1;
        canvas.scale(scale, scale, w / 2, h / 2);
    }
}