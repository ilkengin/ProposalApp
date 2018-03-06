package com.ilkengin.proposalapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.ilkengin.proposalapp.R;
import com.ilkengin.proposalapp.holders.AvailableVideosListViewHolder;
import com.ilkengin.proposalapp.items.AvailableVideosListItem;
import com.ilkengin.proposalapp.utils.AvailableVideosUtils;
import com.ilkengin.proposalapp.utils.FileOperations;
import com.ilkengin.proposalapp.utils.Utils;

import java.util.List;


public class AvailableVideosListAdapter extends BaseAdapter {

    private List<AvailableVideosListItem> availableVideos;

    private Context context;

    public AvailableVideosListAdapter(Context context, List<AvailableVideosListItem> items) {
        this.context = context;
        this.availableVideos = items;
    }

    @Override
    public int getCount() {
        return availableVideos.size();
    }

    @Override
    public Object getItem(int i) {
        return availableVideos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.item_list_available_videos,viewGroup,false);



        AvailableVideosListViewHolder holder = new AvailableVideosListViewHolder(rootView);
        AvailableVideosListItem item = (AvailableVideosListItem) getItem(i);

        holder.getVideoName().setText(item.getVideoName());

        //String filePath = "android.resource://com.cpt.sample/raw/"  + item.getFileName() + ".mp4";
        String filePath = FileOperations.getFileFromRaw(context,item.getFileName()).getAbsolutePath();

        Bitmap thumb = AvailableVideosUtils.getThumbnail(filePath);
        holder.getPreview().setImageBitmap(thumb);

        //Glide.with(context).load("").into(holder.getPreview());



        return rootView;
    }

    public void setAvailableVideos(List<AvailableVideosListItem> availableVideos) {
        this.availableVideos = availableVideos;
    }
}
