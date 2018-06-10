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

import com.example.chai.minrec.Managers.MediaManagerImpl;
import com.example.chai.minrec.Managers.RecordManagerImpl;

import java.io.IOException;

public class MainViewModel extends AndroidViewModel {

    private String mFileName;
    private boolean recording;
    private boolean playing;
    public MutableLiveData<Boolean> recordingLive = new MutableLiveData<>();
    public MutableLiveData<Boolean> playingLive = new MutableLiveData<>();
    private static final String TAG = "Recording";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Handler handler = new Handler();
    private long startTime = 0;
    private RecordManagerImpl recordManager;
    private MediaManagerImpl mediaManager;


    public MainViewModel(@NonNull Application application) {
        super(application);
        recordManager = RecordManagerImpl.getInstance(application);
        mediaManager = MediaManagerImpl.getInstance();
    }


    public void setup() {
        mFileName = recordManager.fileName("/test_file.3gp");
    }

    public void record() {
        recordManager.record(mFileName);
    }

    public void stop() {
        recordManager.stop();
    }

    public void startPlaying() {
        mediaManager.startPlaying(mFileName);
    }


    public void stopPlaying() {
        mediaManager.stopPlaying();
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
        recordManager.recorderCleanup();

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
