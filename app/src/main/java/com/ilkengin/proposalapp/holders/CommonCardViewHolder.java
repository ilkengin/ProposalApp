package com.ilkengin.proposalapp.holders;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.google.android.gms.vision.text.Line;
import com.ilkengin.proposalapp.R;

/**
 * Created by ilkengin on 10.09.2017.
 */

public class CommonCardViewHolder {
    public RelativeLayout imageCardItemLayout;
    public RelativeLayout textCardItemLayout;
    public RelativeLayout videoCardItemLayout;
    public TextView name;
    public TextView district;
    public ImageView image;
    public TextView title;
    public TextView text;
    public TextView pinEnterText;
    public TextView description;

    public EasyVideoPlayer videoPlayer;

    public CommonCardViewHolder(View view) {
        this.imageCardItemLayout = (RelativeLayout) view.findViewById(R.id.item_image_card);
        this.textCardItemLayout = (RelativeLayout) view.findViewById(R.id.item_text_card);
        this.videoCardItemLayout = (RelativeLayout) view.findViewById(R.id.item_video_card);

        this.name = (TextView) view.findViewById(R.id.item_card_name);
        this.district = (TextView) view.findViewById(R.id.item_card_district);
        this.description = (TextView) view.findViewById(R.id.item_card_image_description);
        this.image = (ImageView) view.findViewById(R.id.item_card_image);

        this.title = (TextView) view.findViewById(R.id.item_welcome_card_title);
        this.text = (TextView) view.findViewById(R.id.item_welcome_card_text);
        this.pinEnterText = (TextView) view.findViewById(R.id.item_card_pin_enter_text);

        this.videoPlayer = (EasyVideoPlayer) view.findViewById(R.id.item_card_video_player);
    }
}
