package com.example.tuitionapp;

public class ReviewCurrentTutorList {

    String Review , Overall;

    public String getOverall() {
        return Overall;
    }

    public void setOverall(String overall) {
        Overall = overall;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        this.Review = review;
    }

    public ReviewCurrentTutorList() {
    }

    public ReviewCurrentTutorList(String review) {
        this.Review = review;
    }
}
