package com.validator.mymeds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Triggers notification when pendingIntent is instantiated from MainScreenActivity
 * This class helps send notifications for when a user is to take their medication.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper mNotificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1notification("Alert!!!","Please take medicine ");
        mNotificationHelper.getmanager().notify(1,nb.build());
    }



}
