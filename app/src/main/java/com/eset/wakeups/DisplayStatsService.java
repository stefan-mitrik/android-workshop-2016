package com.eset.wakeups;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class DisplayStatsService extends Service
{
    public static final String EXTRA_SCREEN_STATE = "EXTRA_SCREEN_STATE";

    private BroadcastReceiver mReceiver;
    private PersistenceHelper mPersistenceHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initBroadcastReceiver();
        mPersistenceHelper = new PersistenceHelper(this);

        // TODO 8: start foreground service
    }

    private void initBroadcastReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new ScreenBroadcastReceiver();
        registerReceiver(mReceiver, filter);
    }

    private Notification getNotification(int wakeUpsCount)
    {
        String title = getResources().getString(R.string.notification_header);
        String body = getResources().getString(R.string.notification_body, wakeUpsCount);

        // TODO 9: create notification intent

        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    private void refreshNotification(int wakeUpsNumber)
    {
        // TODO 10: refresh notification
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
