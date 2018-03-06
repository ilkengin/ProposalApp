package com.ilkengin.proposalapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.adapters.CommonCardViewAdapter;
import com.ilkengin.proposalapp.listeners.IMissionCompletedListener;
import com.ilkengin.proposalapp.tasks.SendEmailAsyncTask;
import com.ilkengin.proposalapp.utils.CardOperations;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.utils.Paths;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

public class StoryActivity extends AppCompatActivity implements IMissionCompletedListener {

    private static final String TAG = StoryActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private CommonCardViewAdapter cardViewAdapter;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);

        cardStackView = (CardStackView) findViewById(R.id.activity_story_card_stack_view);
        progressBar = (ProgressBar) findViewById(R.id.activity_story_progress_bar);

        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        cardViewAdapter = getStoriesCardAdapter(cardStackView, sharedPref.getInt(Constants.LAST_COMPLETED_MISSION_KEY,0));
        cardStackView.setAdapter(cardViewAdapter);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardStackView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);

        cardStackView.setCardEventListener(cardViewAdapter);
        cardViewAdapter.setMissionCompletedListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.available_videos_menu_item:
                startActivity(new Intent(this,AvailableVideosActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                CardOperations.swipe(cardStackView,SwipeDirection.Right);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private CommonCardViewAdapter getStoriesCardAdapter(CardStackView view, int lastCompletedMission) {
        //if the stories are to be read from local, use this otherwise use URL PATH
        return new CommonCardViewAdapter(StoryActivity.this,view,Paths.STORIES_FILE_PATH, lastCompletedMission, false);
        //return new CommonCardViewAdapter(StoryActivity.this,view, Paths.STORIES_URL_PATH, lastCompletedMission, true);
    }


    @Override
    public void onMissionCompleted(int number) {
        //here I wanted to be informed about the current status, i.e. which mission is in progress right now.
        sharedPref.edit().putInt(Constants.LAST_COMPLETED_MISSION_KEY,number).apply();
        new SendEmailAsyncTask(getResources().getString(R.string.email),getResources().getString(R.string.password)).execute("Mission Completed!","Mission " + number + " is completed!",getResources().getString(R.string.email));
    }
}
