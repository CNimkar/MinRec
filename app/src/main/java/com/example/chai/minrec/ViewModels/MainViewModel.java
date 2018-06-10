package com.example.chai.minrec.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

public class MainViewModel extends AndroidViewModel {


    private boolean recording;
    private boolean playing;
    public MutableLiveData<Boolean> recordingLive = new MutableLiveData<>();
    public MutableLiveData<Boolean> playingLive = new MutableLiveData<>();
    private static final String TAG = "Recording";
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String mFileName;
    private Handler handler = new Handler();
    private long startTime = 0;


    public MainViewModel(@NonNull Application application) {
        super(application);

    }


    public void setup() {
        mFileName = getApplication().getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";


    }

    public void record() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(mFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mediaRecorder.start();

        Log.i(TAG, "Started recording at" + mFileName);

    }

    public void stop() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;


        Log.i(TAG, "Stopped recording at" + mFileName);
    }

    public void startPlaying() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();

            startTime = SystemClock.elapsedRealtime();
            handler.postDelayed(clockWorkRunnable, 0);


        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        Log.i(TAG, "start playing");
    }


    public void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        handler.removeCallbacks(clockWorkRunnable);
        Log.i(TAG, "Stop playing! ");
    }

    public void clockWork() {
        long time = (startTime < 0) ? 0 : (SystemClock.elapsedRealtime() - startTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int hours = (int) minutes / 60;
        // timeText.setText(hours+":"+minutes+":"+(seconds < 10 ? "0"+seconds : seconds));
    }

    Runnable clockWorkRunnable = () -> clockWork();

    public void checkBeforeStop() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playDecision() {
        if (mFileName == null)
            return;

        if (!playing)
            startPlaying();
        else
            stopPlaying();

        playing = !playing;
        playingLive.postValue(playing);
    }

    public void checkState() {

        if (!recording) {
            record();
        } else {
            stop();
        }

        recording = !recording;
        recordingLive.postValue(recording);

    }


}
