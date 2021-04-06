package com.validator.mymeds;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Class assists in setting up notifications to alert user to take medication.
 * NotificationHelper is instantiated in MainScreenActivity
 * It creates the necessary channels for notifications.
 */
public class NotificationHelper extends ContextWrapper {
    public final String Channel1ID = "Channel1ID";
    public final String Channel1Name = "Channel 1";
    public final String Channel2ID = "Channel2ID";
    public final String Channel2Name = "Channel 2";
    public NotificationManager notificationManager;
    public NotificationHelper (Context base)
    {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createchannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    /**
     * Creates the channels required to alert the user.
     */
    public void createchannel()
    {
        NotificationChannel channel1 = new NotificationChannel(Channel1ID,Channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getmanager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(Channel2ID,Channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getmanager().createNotificationChannel(channel2);
    }

    /**
     * Creates NotificationManager object.
     * @return manager object as retrieved from the SystemService.
     */
    public NotificationManager getmanager()
    {
        if(notificationManager == null)
        {
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /**
     * Method sets up content to be displayed in a notification.
     * @param title refers to notification heading, as it appears on android.
     * @param message refers to notification body, as it appears on android.
     * @return NotificationCompat.Builder object using current application context
     *          and specified message heading/body.
     */
    public NotificationCompat.Builder getChannel1notification(String title,String message)
    {
        return new NotificationCompat.Builder(getApplicationContext(),Channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_ic1);
    }
}
