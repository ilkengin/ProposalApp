package com.ilkengin.proposalapp.items;


public class CardItem {
    private String type;

    //Welcome
    private String title;
    private String text;

    //Story
    private String name;
    private String district;
    private String imageUrl;
    private double latitude;
    private double longtitude;
    private boolean isSwipeable;
    private String pin;
    private String description;

    private String videoUrl;
    private String videoFileName;

    private int missionNumber;

    public CardItem() {

    }

    public CardItem(String type, String title, String text) {
        this.type = type;
        this.title = title;
        this.text = text;
    }

    public CardItem(String type, String name, String district, String imageUrl, double latitude, double longtitude, boolean isSwipeable) {
        this.type = type;
        this.name = name;
        this.district = district;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.isSwipeable = isSwipeable;
    }

    public CardItem(String type, String title, String text, String name, String district, String imageUrl, double latitude, double longtitude, boolean isSwipeable, String pin) {
        this.type = type;
        this.title = title;
        this.text = text;
        this.name = name;
        this.district = district;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.isSwipeable = isSwipeable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public boolean isSwipeable() {
        return isSwipeable;
    }

    public void setSwipeable(boolean swipeable) { this.isSwipeable = swipeable; }

    public String getPin() { return pin; }

    public void setPin(String pin) { this.pin = pin; }

    public String getVideoUrl() { return videoUrl; }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoFileName() {
        return videoFileName;
    }

    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }

    public int getMissionNumber() { return missionNumber; }

    public void setMissionNumber(int missionNumber) { this.missionNumber = missionNumber; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
