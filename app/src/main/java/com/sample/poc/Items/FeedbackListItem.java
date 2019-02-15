package com.sample.poc.Items;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class FeedbackListItem {

    private String imageURL;
    private String userName;
    private int rating;
    private int imageResource;
    private String feedbackData;


    public FeedbackListItem(String imageURL, String userName, int rating, String feedbackData,int imageResource) {
        this.imageURL = imageURL;
        this.userName = userName;
        this.rating = rating;
        this.feedbackData = feedbackData;
        this.imageResource = imageResource;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedbackData() {
        return feedbackData;
    }

    public int getImageResource() {
        return imageResource;
    }
}
