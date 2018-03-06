package com.ilkengin.proposalapp.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilkengin.proposalapp.R;

/**
 * Created by ilkengin on 14.09.2017.
 */

public class AvailableVideosListViewHolder {

    private ImageView preview;
    private TextView videoName;

    public AvailableVideosListViewHolder(View rootView) {
        preview = (ImageView) rootView.findViewById(R.id.preview);
        videoName = (TextView) rootView.findViewById(R.id.videoName);
    }

    public ImageView getPreview() {
        return preview;
    }

    public TextView getVideoName() {
        return videoName;
    }
}
