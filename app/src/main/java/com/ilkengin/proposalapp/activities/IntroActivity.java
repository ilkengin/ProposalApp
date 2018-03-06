package com.ilkengin.proposalapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.utils.Constants;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNextArrowColor(getResources().getColor(R.color.colorPrimary));
        setColorDoneText(getResources().getColor(R.color.colorPrimary));
        setColorSkipButton(getResources().getColor(R.color.colorPrimary));

        setSkipText("GEÇ");
        setDoneText("BİTTİ");

        addSlide(AppIntroFragment.newInstance("Welcome!","sans","This is intro to explain the app!","bold",
                R.mipmap.ic_launcher,
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Welcome!","sans","You can edit json files in resources to make it to fit your idea!","bold",
                R.mipmap.ic_launcher,
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Welcome!","sans","After this intro files, you will see a video and answer some questions. Those are to make the user happier :)","bold",
                R.mipmap.ic_launcher,
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        saveFinished();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        saveFinished();
        finish();
    }

    private void saveFinished() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        //  Make a new preferences editor
        SharedPreferences.Editor e = sharedPreferences.edit();
        //  Edit preference to make it false because we don't want this to run again
        e.putBoolean(Constants.IS_FIRST_START_KEY, false);
        //  Apply changes
        e.apply();
    }
}
