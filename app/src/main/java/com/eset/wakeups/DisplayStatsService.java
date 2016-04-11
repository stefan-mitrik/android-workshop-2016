package com.eset.wakeups;

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
    }

    private void initBroadcastReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new ScreenBroadcastReceiver();
        registerReceiver(mReceiver, filter);
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
        // TODO 6: calculated last wake up duration
        return 0;
    }
}
