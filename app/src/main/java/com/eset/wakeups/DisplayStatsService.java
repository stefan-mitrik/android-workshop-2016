package com.eset.wakeups;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class DisplayStatsService extends Service
{
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initBroadcastReceiver();
    }

    private void initBroadcastReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);

        // TODO 2: add SCREEN_OFF action to intent filter

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
}
