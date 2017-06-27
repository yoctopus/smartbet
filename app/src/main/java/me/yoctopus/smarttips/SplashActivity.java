/*
 * Copyright (C) 2017 Vincent Peter
 * Licensed under the Apache License, Version 2.0 Smart Bet Tips
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.yoctopus.smarttips;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    protected FrameLayout frame;
    protected TextView textView;
    TextView text_splash;
    private Timer timer;
    private ProgressBar progressBar;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setProgress(0);
        text_splash.setText("");
        animateFrame();
    }

    private void animateFrame() {
        final long period = 100;
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (i < 100) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String text = String.valueOf(i).concat("%");
                                    text_splash.setText(text);
                                }
                            });
                            progressBar.setProgress(i);
                            i++;
                        } else {
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                0,
                period);
    }

    private void initView() {
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        frame = (FrameLayout) findViewById(R.id.fl);
        textView = (TextView) findViewById(R.id.textView);
        text_splash = (TextView) findViewById(R.id.progressView);
    }
}


  /*  @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedStart(HIDE_DELAY_MILLIS);
    }


    private void delayedStart(int delayMillis) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
            }
        };
        new Handler().postDelayed(runnable,
                delayMillis);
        */



