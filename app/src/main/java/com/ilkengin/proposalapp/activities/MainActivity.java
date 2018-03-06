package com.ilkengin.proposalapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.adapters.CommonCardViewAdapter;
import com.ilkengin.proposalapp.threads.AppIntroInitializerThread;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.utils.Paths;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Quadrant;

public class MainActivity extends AppCompatActivity implements CardStackView.CardEventListener {

    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private CommonCardViewAdapter cardViewAdapter;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);

        new AppIntroInitializerThread(sharedPref,this).start();

        if (sharedPref.getBoolean(Constants.IS_SECURITY_MISSION_COMPLETE_KEY, false)) {
            startActivity(new Intent(MainActivity.this, StoryActivity.class));
            finish();
        } else {
            setup();
        }
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
                startActivity(new Intent(MainActivity.this,AvailableVideosActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setup() {

        progressBar = findViewById(R.id.activity_main_progress_bar);
        cardStackView = findViewById(R.id.activity_main_card_stack_view);

        cardStackView.setCardEventListener(this);
        cardViewAdapter = createWelcomeCardAdapter();
        cardStackView.setAdapter(cardViewAdapter);

        if (sharedPref.contains(Constants.CURRENT_SECURITY_QUESTIONNAIRE_KEY)) {
            cardStackView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle args = new Bundle();
                    //if the questions are to be read from local, use this otherwise use URL PATH
                    args.putString(Constants.ARG_FILE_NAME, Paths.SECURITY_QUESTIONS_FILE_PATH);
                    //args.putString(Constants.ARG_URL, Paths.SECURITY_QUESTIONS_URL_PATH);
                    Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
                    intent.putExtras(args);
                    startActivityForResult(intent, 1);
                }
            }, 1000);
        } else {
            cardStackView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    cardStackView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    private CommonCardViewAdapter createWelcomeCardAdapter() {
        //if the welcome cards are to be read from local, use this otherwise use URL PATH
        return new CommonCardViewAdapter(MainActivity.this, cardStackView, Paths.WELCOME_CARDS_FILE_PATH,0,false);
        //return new CommonCardViewAdapter(MainActivity.this, cardStackView, Paths.WELCOME_CARDS_URL_PATH,0,true);
    }

    @Override
    public void onCardDragging(float percentX, float percentY) {
    }

    @Override
    public void onCardSwiped(Quadrant quadrant) {
        if (cardStackView.getTopIndex() == cardViewAdapter.getCount()) {

            cardStackView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            Bundle args = new Bundle();
            //if the questions are to be read from local, use this otherwise use URL PATH
            args.putString(Constants.ARG_FILE_NAME, Paths.SECURITY_QUESTIONS_FILE_PATH);
            //args.putString(Constants.ARG_URL, Paths.SECURITY_QUESTIONS_URL_PATH);
            Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
            intent.putExtras(args);
            startActivityForResult(intent, 1);

        }
    }

    @Override
    public void onCardReversed() {
    }

    @Override
    public void onCardMovedToOrigin() {
    }

    @Override
    public void onCardClicked(int index) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    sharedPref.edit()
                            .putBoolean(Constants.IS_SECURITY_MISSION_COMPLETE_KEY, true)
                            .apply();
                    startActivity(new Intent(this, StoryActivity.class));
                    finish();
                } else if (resultCode == RESULT_CANCELED) {
                    cardViewAdapter = createWelcomeCardAdapter();
                    cardStackView.setAdapter(cardViewAdapter);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cardStackView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 1000);


                }
                break;
        }
    }
}
