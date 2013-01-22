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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.SeekBar;
import android.widget.TextView;
import com.vmladenov.cook.CustomApplication;
import com.vmladenov.cook.R;

public class CustomCountDown extends CountDownTimer {
    final int tenSeconds = 10000;
    private final String startTime = "00:00 минути";
    public TextView tvTimer;
    public SeekBar sbTime;
    private final long millisInFuture;
    private final Context context;
    private NotificationManager notificationManager;
    private Notification notification;
    private PendingIntent pendingIntent;
    public Boolean isWorking;

    public CustomCountDown(Context context, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.millisInFuture = millisInFuture;
        isWorking = false;
    }

    @Override
    public void onFinish() {
        playNotification();
        tvTimer.setText(startTime);
        sbTime.setProgress(0);
        notificationManager.cancel(1);
        CustomApplication.getInstance().customCountDown = null;
        isWorking = false;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        sbTime.setProgress((int) (millisUntilFinished / tenSeconds));
        String txt = toReadableText(millisUntilFinished);
        tvTimer.setText(txt);
        notification.setLatestEventInfo(context.getApplicationContext(),
                context.getText(R.string.app_name), txt, pendingIntent);
        notificationManager.notify(1, notification);
    }

    public void startTimer() {
        String txt = toReadableText(millisInFuture);
        String ns = Context.NOTIFICATION_SERVICE;
        notificationManager = (NotificationManager) context.getSystemService(ns);
        notification = new Notification(R.drawable.icon, txt, System.currentTimeMillis());
        Intent intent = new Intent(context, TimerActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context.getApplicationContext(),
                context.getText(R.string.app_name), txt, pendingIntent);

        super.start();
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        notificationManager.notify(1, notification);
        // bStartTimer.setEnabled(false);
        isWorking = true;
    }

    public void stopTimer() {
        this.cancel();
        notificationManager.cancelAll();
        isWorking = false;
    }

    private String toReadableText(long miliseconds) {
        int seconds = (int) miliseconds / 1000;
        int minutesPart = seconds / 60;
        int secondsPart = seconds - (minutesPart * 60);
        String minutesStr = String.valueOf(minutesPart);
        if (minutesPart < 10) minutesStr = "0" + minutesStr;
        String secondsStr = String.valueOf(secondsPart);
        if (secondsPart < 10) secondsStr = "0" + secondsStr;
        return minutesStr + ":" + secondsStr + " минути";
    }

    private void playNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(ns);
        Notification notification = new Notification(R.drawable.icon, startTime,
                System.currentTimeMillis());

        Intent intent = new Intent(context, TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context.getApplicationContext(),
                context.getText(R.string.app_name), startTime, pendingIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_INSISTENT;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(2, notification);
    }
}
