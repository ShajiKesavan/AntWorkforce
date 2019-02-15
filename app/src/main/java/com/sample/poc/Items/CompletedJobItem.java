package com.sample.poc.Items;

/**
 * Created by 1013373 on 8/3/2018.
 */

public class CompletedJobItem {

    private String startDate;
    private String startTime;
    private String role;
    private String rate;
    private String duration;
    private String name;
    private String approvalStatus;

    public CompletedJobItem(String startDate, String startTime, String role, String rate, String duration, String name, String approvalStatus) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.role = role;
        this.rate = rate;
        this.duration = duration;
        this.name = name;
        this.approvalStatus = approvalStatus;
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

    public String getName() {
        return name;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }
}
