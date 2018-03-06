package com.ilkengin.proposalapp.items;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

@JsonObject
public class AvailableVideosListItem implements Serializable {

    @JsonField
    private String fileName;
    @JsonField
    private String videoName;

    public AvailableVideosListItem() {
    }

    public AvailableVideosListItem(String fileName, String videoName) {
        this.fileName = fileName;
        this.videoName = videoName;
    }

    public AvailableVideosListItem(CardItem item) {
        this.fileName = item.getVideoFileName();
        this.videoName = item.getName();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != AvailableVideosListItem.class) {
            return false;
        }

        AvailableVideosListItem item = (AvailableVideosListItem) obj;

        boolean areFileNamesMatch = false;
        boolean areVideoNamesMatch = false;


        if((this.videoName == null && item.videoName == null ) ||
                (this.videoName != null && this.videoName.equals(item.videoName))) {
            areVideoNamesMatch = true;
        }

        if((this.fileName == null && item.fileName == null ) ||
                (this.fileName != null && this.fileName.equals(item.fileName))) {
            areFileNamesMatch = true;
        }

        return (areFileNamesMatch && areVideoNamesMatch);
    }
}
