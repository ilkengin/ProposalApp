package com.ilkengin.proposalapp.utils;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.ilkengin.proposalapp.items.AvailableVideosListItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ilkengin.proposalapp.utils.Constants.AVAILABLE_VIDEOS_KEY;

public class AvailableVideosUtils {

    private static final String TAG = AvailableVideosUtils.class.getSimpleName();

    public static List<AvailableVideosListItem> getAvailableVideos(SharedPreferences sharedPref) {
        Log.d(TAG,"getAvailableVideos started");

        List<AvailableVideosListItem> items = new ArrayList<>();
        Set<String> videosSet = sharedPref.getStringSet(AVAILABLE_VIDEOS_KEY,new HashSet<String>());

        for(String video : videosSet) {
            Log.d(TAG,"Old video : " + video);
            try {
                AvailableVideosListItem itemFromString = LoganSquare.parse(video, AvailableVideosListItem.class);
                items.add(itemFromString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG,"getAvailableVideos ended");
        return items;
    }

    public static void addToAvailableVideos(SharedPreferences sharedPref, AvailableVideosListItem video) {
        Log.d(TAG,"addToAvailableVideos started");
        Set<String> videosSet = sharedPref.getStringSet(AVAILABLE_VIDEOS_KEY,new HashSet<String>());

        for(String item : videosSet) {
            Log.d(TAG,"Old video : " + item);
            try {
                AvailableVideosListItem availableVideosListItem = LoganSquare.parse(item, AvailableVideosListItem.class);
                if(availableVideosListItem.equals(video)) {
                    Log.d(TAG,"Old video : " + item);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String itemString = LoganSquare.serialize(video);
            Log.d(TAG,"Video is put to set : " + itemString);
            videosSet.add(itemString);
            sharedPref.edit().putStringSet(AVAILABLE_VIDEOS_KEY,videosSet).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"addToAvailableVideos ended");

    }

    public static Bitmap getThumbnail(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
    }

}
