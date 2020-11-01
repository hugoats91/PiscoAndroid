package com.promperu.pisco.Utils;

import android.content.Context;
import android.media.MediaPlayer;

public class UtilSound {

    public static void startSound(Context context, int sound){
        MediaPlayer mp = MediaPlayer.create(context, sound);
        mp.setOnCompletionListener(MediaPlayer::release);
        mp.start();
    }

}