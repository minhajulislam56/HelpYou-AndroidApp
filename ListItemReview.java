package com.minhajisartisticgmail.helpyou;

/**
 * Created by my on 5/25/2018.
 */

public class ListItemReview {

    private String name;
    private float rating;
    private String comment;

    public ListItemReview(String name, float rating, String comment) {

        this.name = name;
        this.rating = rating;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ListItemReview(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
