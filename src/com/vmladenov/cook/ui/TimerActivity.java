/*
 * Copyright (C) 2011 Ventsislav Mladenov <ventsislav dot mladenov at gmail dot com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.vmladenov.cook.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ZoomControls;
import com.vmladenov.cook.CustomApplication;
import com.vmladenov.cook.R;

public class TimerActivity extends Activity implements OnSeekBarChangeListener {
    private TextView tvTimer;
    private SeekBar sbTime;
    private Button bStartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.timer_view);
        this.setTitle(R.string.coockingTimer);
        tvTimer = (TextView) findViewById(R.id.tvTime);
        bStartTimer = (Button) findViewById(R.id.bStartTimer);
        sbTime = (SeekBar) findViewById(R.id.sbTime);
        sbTime.setOnSeekBarChangeListener(this);
        ZoomControls zcTimer = (ZoomControls) findViewById(R.id.zcTimer);
        zcTimer.setOnZoomInClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int progress = sbTime.getProgress();
                if (sbTime.getMax() > 30) {
                    sbTime.setMax(sbTime.getMax() / 2);
                    progress = progress / 2;
                    sbTime.setProgress(progress);
                    progressToTime(progress);
                }
            }
        });

        zcTimer.setOnZoomOutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int progress = sbTime.getProgress();
                if (sbTime.getMax() < 960) {
                    sbTime.setMax(sbTime.getMax() * 2);
                    progress = progress * 2;
                    sbTime.setProgress(progress);
                    progressToTime(progress);
                }
            }
        });

        CustomCountDown customCountDown = CustomApplication.getInstance().customCountDown;
        if (customCountDown != null) {
            // customCountDown.bStartTimer = bStartTimer;
            customCountDown.tvTimer = tvTimer;
            customCountDown.sbTime = sbTime;
        }
        setStartStopButton();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            progressToTime(progress);
        }
    }

    private void progressToTime(int progress) {
        int minutes = progress / 6;
        int seconds = (progress % 6) * 10;
        String minutesStr = String.valueOf(minutes);
        if (minutes < 10) minutesStr = "0" + minutesStr;
        String secondsStr = String.valueOf(seconds);
        if (seconds == 0) secondsStr = "00";
        tvTimer.setText(minutesStr + ":" + secondsStr + " минути");
    }

    public void onStartCountDown(View sender) {
        CustomCountDown customCountDown = CustomApplication.getInstance().customCountDown;
        if (customCountDown != null && customCountDown.isWorking) {
            customCountDown.stopTimer();
            CustomApplication.getInstance().customCountDown = null;
        } else {
            if (sbTime.getProgress() < 1) return;
            final int tenSeconds = 10000;
            int timeToCountDown = sbTime.getProgress() * tenSeconds;
            customCountDown = new CustomCountDown(TimerActivity.this, timeToCountDown, 1000);
            CustomApplication.getInstance().customCountDown = customCountDown;
            customCountDown.sbTime = sbTime;
            customCountDown.tvTimer = tvTimer;
            customCountDown.startTimer();
        }
        setStartStopButton();
    }

    private void setStartStopButton() {
        CustomCountDown customCountDown = CustomApplication.getInstance().customCountDown;
        if (customCountDown != null && customCountDown.isWorking) {
            bStartTimer.setText(R.string.stopTimer);
        } else {
            bStartTimer.setText(R.string.startTimer);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
