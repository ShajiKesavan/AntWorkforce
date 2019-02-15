package com.sample.poc.Items;

/**
 * Created by 1013373 on 7/31/2018.
 */

public class PostedJobItems {

    private String startDate;
    private String startTime;
    private String role;
    private String rate;
    private String duration;
    private int postsCount;
    private int acceptedCount;
    private int confirmedCount;
    private int interestReceivedCount;


    public PostedJobItems(String startDate, String startTime, String role, String rate, String duration, int postsCount, int acceptedCount, int confirmedCount, int interestReceivedCount) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.role = role;
        this.rate = rate;
        this.duration = duration;
        this.postsCount = postsCount;
        this.acceptedCount = acceptedCount;
        this.confirmedCount = confirmedCount;
        this.interestReceivedCount = interestReceivedCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getRole() {
        return role;
    }

    public String getRate() {
        return rate;
    }

    public String getDuration() {
        return duration;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public int getAcceptedCount() {
        return acceptedCount;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public int getInterestReceivedCount() {
        return interestReceivedCount;
    }
}
