package com.sample.poc.Items;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class ListJobsItem {

    private String startDate;
    private String startTime;
    private String role;
    private String rate;
    private String duration;
    private String distance;
    private String employer;
    private String description;
    private int rateInt;
    private int rateDec;


    public ListJobsItem(String startDate, String startTime, String role, String rate, String duration, String distance, String employer, String description,int rateInt,int rateDec) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.role = role;
        this.rate = rate;
        this.duration = duration;
        this.distance = distance;
        this.employer = employer;
        this.description = description;
        this.rateInt=rateInt;
        this.rateDec=rateDec;
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

    public int getRateInt() {
        return rateInt;
    }

    public int getRateDec() {
        return rateDec;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    public String getEmployer() {
        return employer;
    }

    public String getDescription() {
        return description;
    }
}
