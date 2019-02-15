package com.sample.poc.Items;

import java.util.ArrayList;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class InterestListItem {

    private String name;
    private String acceptableRate;
    private String regNo;
    private String experience;
    private String imageURL;
    private int rating;
    private int imageResource;
    private ArrayList<FeedbackListItem> feedbackListItems;


    public InterestListItem(String name, String acceptableRate, String regNo, String experience, String imageURL, int rating,int imageResource,ArrayList<FeedbackListItem> feedbackListItems) {
        this.name = name;
        this.acceptableRate = acceptableRate;
        this.regNo = regNo;
        this.experience = experience;
        this.imageURL = imageURL;
        this.rating = rating;
        this.imageResource=imageResource;
        this.feedbackListItems=feedbackListItems;
    }

    public String getName() {
        return name;
    }

    public String getAcceptableRate() {
        return acceptableRate;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getExperience() {
        return experience;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getRating() {
        return rating;
    }


    public ArrayList<FeedbackListItem> getFeedbackListItems() {
        return feedbackListItems;
    }
}
