package com.ilkengin.proposalapp.threads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ilkengin.proposalapp.activities.IntroActivity;
import com.ilkengin.proposalapp.utils.Constants;

public class AppIntroInitializerThread extends Thread {

    private SharedPreferences sharedPreferences;
    private Activity activity;

    public AppIntroInitializerThread(SharedPreferences sharedPreferences, Activity activity) {
        this.sharedPreferences = sharedPreferences;
        this.activity = activity;
    }

    @Override
    public void run() {

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = sharedPreferences.getBoolean(Constants.IS_FIRST_START_KEY, true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(activity, IntroActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    activity.runOnUiThread(new Runnable() {
                        @Override public void run() {
                            activity.startActivity(i);
                        }
                    });
                }
            }
        });

        // Start the thread
        t.start();
    }
}
