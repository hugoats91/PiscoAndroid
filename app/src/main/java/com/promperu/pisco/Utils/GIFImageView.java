package com.promperu.pisco.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GIFImageView extends View {

    private InputStream inputStream;
    private Movie movie;
    private int width, height;
    private long start;
    private Context context;
    private Paint transparentPaint;

    public GIFImageView(Context context) {
        super(context);
        this.context = context;
    }

    public GIFImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
    }

    public GIFImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (attrs.getAttributeName(1).equals("background")) {
            int id = Integer.parseInt(attrs.getAttributeValue(1).substring(1));
            setGifImageResource(id);
        }
        transparentPaint = new Paint();
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
    }

    private void init() {
        setFocusable(true);
        movie = Movie.decodeStream(inputStream);
        width = movie.width();
        height = movie.height();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = SystemClock.uptimeMillis();
        if (start == 0) {
            start = now;
        }
        if (movie != null) {
            int duration = movie.duration();
            if (duration == 0) {
                duration = 4000;
            }
            int relTime = (int) ((now - start) % duration);
            movie.setTime(relTime);
            movie.draw(canvas, 0, 0,transparentPaint);
            invalidate();
        }
    }

    public void setGifImageResource(int id) {
        inputStream = context.getResources().openRawResource(id);
        init();
    }

    public void setSize(int dimension) {
        width = dimension;
        height = dimension;
    }

}
