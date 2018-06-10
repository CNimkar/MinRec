package com.example.chai.minrec.Managers;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

public class MediaManagerImpl implements MediaManager {

    private static final String TAG = "Recording";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();
    private long startTime = 0;
    private static MediaManagerImpl instance;

    private MediaManagerImpl() {
    }

    public static MediaManagerImpl getInstance() {
        if (instance != null)
            return instance;

        return new MediaManagerImpl();
    }

    @Override
    public void startPlaying(String fileName) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();

            startTime = SystemClock.elapsedRealtime();
            // handler.postDelayed(clockWorkRunnable, 0);


        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        Log.i(TAG, "start playing");
    }

    @Override
    public void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        //    handler.removeCallbacks(clockWorkRunnable);
        Log.i(TAG, "Stop playing! ");
    }

    @Override
    public void mediaPlayerCleanup() {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
