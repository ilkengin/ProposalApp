package com.ilkengin.proposalapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.activities.MapViewActivity;
import com.ilkengin.proposalapp.activities.PinEnterActivity;
import com.ilkengin.proposalapp.holders.CommonCardViewHolder;
import com.ilkengin.proposalapp.items.AvailableVideosListItem;
import com.ilkengin.proposalapp.items.CardItem;
import com.ilkengin.proposalapp.listeners.CardVideoCallback;
import com.ilkengin.proposalapp.listeners.IHTTPRequestListener;
import com.ilkengin.proposalapp.listeners.IMissionCompletedListener;
import com.ilkengin.proposalapp.listeners.IVideoEndListener;
import com.ilkengin.proposalapp.tasks.GetJsonStringFromUrlAsyncTask;
import com.ilkengin.proposalapp.utils.AvailableVideosUtils;
import com.ilkengin.proposalapp.utils.CardOperations;
import com.ilkengin.proposalapp.utils.Constants;
import com.ilkengin.proposalapp.utils.JsonOperations;
import com.ilkengin.proposalapp.utils.Utils;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Quadrant;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.List;

import static com.ilkengin.proposalapp.utils.Constants.TYPE_IMAGE;
import static com.ilkengin.proposalapp.utils.Constants.TYPE_TEXT;
import static com.ilkengin.proposalapp.utils.Constants.TYPE_VIDEO;

public class CommonCardViewAdapter extends ArrayAdapter implements CardStackView.CardEventListener, IVideoEndListener, IHTTPRequestListener {

    private static final String TAG = CommonCardViewAdapter.class.getSimpleName();

    private List<CardItem> cardItems = new ArrayList<>();

    private CardStackView cardStackView;

    private boolean isSwipeEnabled = true;

    private Activity activity;

    private IMissionCompletedListener missionCompletedListener;

    private int lastCompletedMission;

    public CommonCardViewAdapter(Activity activity, CardStackView cardStackView, String fileNameOrUrl, int lastCompletedMission, boolean isUrl) {
        super(activity,0);
        this.activity = activity;
        this.cardStackView = cardStackView;
        this.lastCompletedMission = lastCompletedMission;

        if(isUrl) {
            new GetJsonStringFromUrlAsyncTask(this).execute(fileNameOrUrl);
        } else {
            List<CardItem> allCardItems = JsonOperations.getItemsFromFile(activity, fileNameOrUrl,CardItem.class);
            List<CardItem> filteredItems = getFilteredList(allCardItems);
            this.cardItems = new ArrayList<>(filteredItems);
        }
    }

    @Override
    public int getCount() {
        return cardItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public CardItem getItem(int position) {
        return cardItems.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        CardItem item = getItem(position);
        if("text".equals(item.getType().toLowerCase())) {
            return TYPE_TEXT;
        } else if("image".equals(item.getType().toLowerCase())) {
            return TYPE_IMAGE;
        } else if("video".equals(item.getType().toLowerCase())) {
            return TYPE_VIDEO;
        }
        else {
            return -1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup parent) {

        CommonCardViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_card_common, parent, false);
            holder = new CommonCardViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (CommonCardViewHolder) contentView.getTag();
        }

        final CardItem item = getItem(position);

        if(getItemViewType(position) == TYPE_TEXT) {

            holder.imageCardItemLayout.setVisibility(View.GONE);
            holder.videoCardItemLayout.setVisibility(View.GONE);
            holder.textCardItemLayout.setVisibility(View.VISIBLE);

            holder.title.setText(item.getTitle());
            holder.text.setText(item.getText());

        } else if(getItemViewType(position) == TYPE_IMAGE) {

            holder.textCardItemLayout.setVisibility(View.GONE);
            holder.videoCardItemLayout.setVisibility(View.GONE);
            holder.imageCardItemLayout.setVisibility(View.VISIBLE);


            holder.imageCardItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putDouble(Constants.ARG_LAT,item.getLatitude());
                    args.putDouble(Constants.ARG_LONG,item.getLongtitude());
                    args.putString(Constants.ARG_TITLE,item.getName());

                    Intent intent = new Intent(activity, MapViewActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(args);
                    getContext().startActivity(intent);
                }
            });

            holder.pinEnterText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(Constants.ARG_PIN,item.getPin());

                    Intent intent = new Intent(activity, PinEnterActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtras(args);

                    activity.startActivityForResult(intent,1);
                }
            });

            if(item.getDescription() != null && !item.getDescription().isEmpty()) {
                holder.description.setText(item.getDescription());
            }

            holder.name.setText(item.getName());
            holder.district.setText(item.getDistrict());
            Glide.with(getContext()).load(item.getImageUrl()).into(holder.image);

        } else if (getItemViewType(position) == TYPE_VIDEO) {
            holder.textCardItemLayout.setVisibility(View.GONE);
            holder.imageCardItemLayout.setVisibility(View.GONE);
            holder.videoCardItemLayout.setVisibility(View.VISIBLE);

            Uri video;

            if(item.getVideoUrl() != null && !item.getVideoUrl().isEmpty()) {
                video = Uri.parse(item.getVideoUrl());
            } else {
                video = Uri.parse("android.resource://" + activity.getPackageName() + "/" + activity.getResources().getIdentifier(item.getVideoFileName(),"raw",activity.getPackageName()));
            }

            holder.videoPlayer.setSource(video);
            holder.videoPlayer.setCallback(new CardVideoCallback(this));

        }
        return contentView;

    }

    private List<CardItem> getFilteredList(List<CardItem> allCardItems) {
        List<CardItem> mCardItems = new ArrayList<>();
        if(lastCompletedMission > 0) {
            mCardItems = new ArrayList<>();
            int i = 0;
            while(allCardItems.get(i).getMissionNumber() < lastCompletedMission) {
                i++;
            }
            i++;
            for(;i < allCardItems.size();i++) {
                mCardItems.add(allCardItems.get(i));
            }
        } else {
            mCardItems.addAll(allCardItems);
        }
        return mCardItems;
    }

    @Override
    public void onCardDragging(float percentX, float percentY) {
        Log.d(TAG,"onCardDragging() with topIndex : " + cardStackView.getTopIndex());
        Log.d(TAG,"isSwipeable is " + getItem(cardStackView.getTopIndex()).isSwipeable());
        if(getItem(cardStackView.getTopIndex()).isSwipeable() != isSwipeEnabled) {
            setSwipeEnabled(getItem(cardStackView.getTopIndex()).isSwipeable());
        }
    }

    @Override
    public void onCardSwiped(Quadrant quadrant) {
        Log.d(TAG,"onCardSwiped() with topIndex : " + cardStackView.getTopIndex());
        setSwipeEnabled(true);
        if(getItem(cardStackView.getTopIndex() - 1).getMissionNumber() > 0 && missionCompletedListener != null) {
            missionCompletedListener.onMissionCompleted(getItem(cardStackView.getTopIndex() - 1).getMissionNumber());
        }
    }

    @Override
    public void onCardReversed() {
        Log.d(TAG,"onCardReversed() with topIndex : " + cardStackView.getTopIndex());
    }

    @Override
    public void onCardMovedToOrigin() {
        Log.d(TAG,"onCardMovedToOrigin() with topIndex : " + cardStackView.getTopIndex());
    }

    @Override
    public void onCardClicked(int index) {
        Log.d(TAG,"onCardClicked() with topIndex : " + cardStackView.getTopIndex());
    }

    private void setSwipeEnabled(boolean isSwipeEnabled) {
        if(this.isSwipeEnabled != isSwipeEnabled) {
            this.isSwipeEnabled = isSwipeEnabled;
            cardStackView.setSwipeEnabled(isSwipeEnabled);
        }
    }

    @Override
    public void onVideoEnd() {
        Log.d(TAG,"onVideoEnd");
        CardItem cardItem = getItem(cardStackView.getTopIndex());
        AvailableVideosUtils.addToAvailableVideos(activity.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE),new AvailableVideosListItem(cardItem));
        CardOperations.swipe(cardStackView, SwipeDirection.Right);
    }

    public void setMissionCompletedListener(IMissionCompletedListener missionCompletedListener) {
        this.missionCompletedListener = missionCompletedListener;
    }

    @Override
    public void onRequestDone(String jsonString) {
        if(jsonString != null) {
            List<CardItem> items = JsonOperations.getItemsFromString(jsonString,CardItem.class);
            List<CardItem> filteredItems = getFilteredList(items);
            this.cardItems = new ArrayList<>(filteredItems);
            this.notifyDataSetChanged();
        }
    }
}
