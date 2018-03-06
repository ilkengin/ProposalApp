package com.ilkengin.proposalapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.ilkengin.proposalapp.R;


public class VideoPlayerFragment extends Fragment implements EasyVideoCallback {

    private static final String TAG = VideoPlayerFragment.class.getSimpleName();

    private static final String TEST_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    private EasyVideoPlayer player;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_player,container,false);

        Log.d(TAG,"onCreateView");


//        player = (EasyVideoPlayer) view.findViewById(R.id.video_fragment_player);
//        player.setCallback(this);
//        player.setSource(Uri.parse(TEST_URL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

        //hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {
        Log.d(TAG,"onPreparing");
    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {
        Log.d(TAG,"onBuffering");

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Log.d(TAG,"onError: " + e.toString());
        e.printStackTrace();
    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        player.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}
