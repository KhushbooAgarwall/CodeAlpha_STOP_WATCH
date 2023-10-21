package com.example.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;


import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class MainActivity extends AppCompatActivity {

    private long milliseconds;
    private boolean running;
    private boolean wasRunning;
    private MaterialProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_countdown);

        if (savedInstanceState != null) {
            milliseconds = savedInstanceState.getLong("milliseconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();
        stopProgressBar();
    }

    public void onStart(View view) {
        running = true;
        if (wasRunning) {
            long currentTime = System.currentTimeMillis();
            long elapsedTimeSincePause = (currentTime - milliseconds) % 1000;
            milliseconds += elapsedTimeSincePause;
        }
        startProgressBar();
    }

    public void onStop(View view) {
        running = false;
        if (wasRunning) {
            milliseconds = System.currentTimeMillis();
            wasRunning = false;
        }
        stopProgressBar();
    }

    private void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(100);

    }

    private void stopProgressBar() {        progressBar.setVisibility(View.INVISIBLE);

    }

    public void onReset(View view) {
        running = false;
        milliseconds = 0;
        stopProgressBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
        stopProgressBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
            startProgressBar();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("milliseconds", milliseconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    private void runTimer() {
        TextView timeView = findViewById(R.id.textView);
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long minutes = (milliseconds / 100) / 60;
                long secs = (milliseconds / 100) % 60;
                long millis = milliseconds % 100;

                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, secs, millis);

                timeView.setText(time);

                if (running) {
                    milliseconds += 1;
                    // Update the progress of the MaterialProgressBar here
                    // You can use progressBar.setProgress(value) to update the progress
                }
                handler.postDelayed(this, 1);
            }
        });
    }
}