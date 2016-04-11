package com.eset.wakeups;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean isScreenOn = intent.getAction().equals(Intent.ACTION_SCREEN_ON);

        if (isScreenOn)
        {
            Toast.makeText(context, context.getResources().getString(R.string.hello_toast), Toast.LENGTH_SHORT).show();
        }

        // TODO 5: inform service about screen on/off broadcasts
    }
}
