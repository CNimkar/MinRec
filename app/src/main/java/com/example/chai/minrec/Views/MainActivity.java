package com.example.chai.minrec.Views;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.chai.minrec.R;
import com.example.chai.minrec.ViewModels.MainViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private boolean permissionToRecordAccepted = false;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.playRecording)
    FloatingActionButton playRec;

    @BindView(R.id.timeText)
    TextView timeText;

    @OnClick(R.id.playRecording)
    void audioStream() {
        viewModel.playDecision();
    }

    @OnClick(R.id.fab)
    void checkState() {
        viewModel.checkState();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                return true;
            case R.id.navigation_dashboard:
                return true;
        }
        return false;
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        ButterKnife.bind(this);


        getSupportActionBar().hide();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setup();
        viewModel.setup();

    }

    private void setup() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        playRec.setVisibility(View.GONE);
        fab.setBackgroundTintList(ColorStateList
                .valueOf(getResources()
                        .getColor(R.color.stoppedColor)));

        viewModel.recordingLive.observe(this, aBoolean -> {
            if (aBoolean)
                recordUI();
            else
                stopRecordingUI();
        });

    }


    private void recordUI() {
        fab.setBackgroundTintList(ColorStateList
                .valueOf(getResources()
                        .getColor(R.color.colorPrimary)));
        fab.setImageResource(R.drawable.ic_stop_black_24dp);

    }

    private void stopRecordingUI() {
        fab.setBackgroundTintList(ColorStateList
                .valueOf(getResources()
                        .getColor(R.color.stoppedColor)));
        fab.setImageResource(R.drawable.ic_mic_black_24dp);
        playRec.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.checkBeforeStop();
    }
}
