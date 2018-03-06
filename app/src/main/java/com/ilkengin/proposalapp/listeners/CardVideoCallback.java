package com.ilkengin.proposalapp.listeners;

import android.net.Uri;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

/**
 * Created by ilkengin on 12.09.2017.
 */

public class CardVideoCallback implements EasyVideoCallback {

    private IVideoEndListener videoEndListener;

    public CardVideoCallback(IVideoEndListener listener) {
        this.videoEndListener = listener;
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        if(videoEndListener != null) {
            videoEndListener.onVideoEnd();
        }
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
