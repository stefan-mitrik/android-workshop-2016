package com.eset.wakeups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity
{
    private PersistenceHelper mPersistenceHelper;

    private TextView mWakeUpsNumberText;
    private TextView mTotalUpTimeText;
    private TextView mLongestStreakText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUiViews();

        mPersistenceHelper = new PersistenceHelper(this);

        startService(new Intent(this, DisplayStatsService.class));
    }

    private void initUiViews()
    {
        mWakeUpsNumberText = (TextView) findViewById(R.id.wake_ups_number_text);
        mTotalUpTimeText = (TextView) findViewById(R.id.wake_up_time_text);
        mLongestStreakText = (TextView) findViewById(R.id.longest_strike_text);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshStats();
    }

    private void refreshStats()
    {
        mWakeUpsNumberText.setText(String.valueOf(mPersistenceHelper.getWakeUpsNumber()));
        mTotalUpTimeText.setText(formatMillisecondsTime(mPersistenceHelper.getTotalUpTime()));
        mLongestStreakText.setText(formatMillisecondsTime(mPersistenceHelper.getLongestStreak()));
    }

    private String formatMillisecondsTime(long millis)
    {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
