package com.example.chai.minrec.Managers;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class RecordManagerImpl implements RecordManager {

    private static final String TAG = "RecordManager";
    private Context context;
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private static RecordManagerImpl instace;

    private RecordManagerImpl(Context context) {
        this.context = context;
    }

    public static RecordManagerImpl getInstance(Context context) {
        if (instace != null)
            return instace;

        return new RecordManagerImpl(context);
    }

    @Override
    public String fileName(String fileName) {
        String mFileName = context.getExternalCacheDir().getAbsolutePath();
        mFileName += fileName;
        return mFileName;
    }

    @Override
    public void record(String fileName) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mediaRecorder.start();

        Log.i(TAG, "Started recording at" + fileName);

    }

    @Override
    public void stop() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;


        Log.i(TAG, "Stopped recording");
    }

    @Override
    public void recorderCleanup() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
