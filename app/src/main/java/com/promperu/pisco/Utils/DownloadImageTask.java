package com.promperu.pisco.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private final ThreadLocal<ImageView> bmImage = new ThreadLocal<>();

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage.set(bmImage);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        ImageView imageView = bmImage.get();
        if (imageView != null) {
            imageView.setImageBitmap(result);
        }
    }

}