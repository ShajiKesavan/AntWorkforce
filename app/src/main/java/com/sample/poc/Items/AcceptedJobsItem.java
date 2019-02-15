package com.sample.poc.Items;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class AcceptedJobsItem {
    private String startDate;
    private String startTime;
    private String role;
    private String rate;
    private String duration;
    private String distance;
    private String employer;
    private String description;

    public AcceptedJobsItem(String startDate, String startTime, String role, String rate, String duration, String distance, String employer, String description) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.role = role;
        this.rate = rate;
        this.duration = duration;
        this.distance = distance;
        this.employer = employer;
        this.description = description;
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
