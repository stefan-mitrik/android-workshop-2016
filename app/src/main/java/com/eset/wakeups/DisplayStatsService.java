package com.eset.wakeups;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class DisplayStatsService extends Service
{
    private static final int NOTIFICATION_ID = 1;
    public static final String EXTRA_SCREEN_STATE = "EXTRA_SCREEN_STATE";

    private BroadcastReceiver mReceiver;
    private PersistenceHelper mPersistenceHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initBroadcastReceiver();
        mPersistenceHelper = new PersistenceHelper(this);

        Notification notification = getNotification(mPersistenceHelper.getWakeUpsNumber());
        startForeground(NOTIFICATION_ID, notification);
    }

    private void initBroadcastReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new ScreenBroadcastReceiver();
        registerReceiver(mReceiver, filter);
    }

    private Notification getNotification(int wakeUpsCount) {
        String title = getResources().getString(R.string.notification_header);
        String body = getResources().getString(R.string.notification_body, wakeUpsCount);

        Intent clickIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);

        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void refreshNotification(int wakeUpsNumber)
    {
        Notification notification = getNotification(wakeUpsNumber);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent != null && intent.hasExtra(EXTRA_SCREEN_STATE))
        {
            if (intent.getBooleanExtra(EXTRA_SCREEN_STATE, false))
            {
                onScreenOn();
            }
            else
            {
                onScreenOff();
            }
        }

        return START_STICKY;
    }

    private void onScreenOn()
    {
        mPersistenceHelper.incrementWakeUpsNumber();
        mPersistenceHelper.setLastWakeUpTime(System.currentTimeMillis());

        refreshNotification(mPersistenceHelper.getWakeUpsNumber());
    }

    private void onScreenOff()
    {
        long upTime = getLastWakeUpDuration();

        mPersistenceHelper.incrementTotalUpTime(upTime);

        if (upTime > mPersistenceHelper.getLongestStreak())
        {
            mPersistenceHelper.setLongestStreak(upTime);
        }
    }

    private long getLastWakeUpDuration()
    {
        long lastWakeUp = mPersistenceHelper.getLastWakeUpTime();
        long now = System.currentTimeMillis();

        return now - lastWakeUp;
    }
}
