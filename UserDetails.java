package com.minhajisartisticgmail.helpyou;

/**
 * Created by MINHAJ on 3/27/2018.
 */

public class UserDetails {
    String name, address, gender, dob, mobileNo, email, userID;
    float rating, reviewer;
    boolean type, status;
    String expert;

    public UserDetails(){

    }

    public UserDetails(String Name, String Address, String expert, String Gender, String DOB, String MobileNo, float Rating, float Reviewer, String Email, String UserID, boolean type, boolean status){
        this.name = Name;
        this.address = Address;
        this.gender = Gender;
        this.dob = DOB;
        this.mobileNo = MobileNo;
        this.rating = Rating;
        this.reviewer = Reviewer;
        this.email = Email;
        this.userID = UserID;
        this.type = type;
        this.expert = expert;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public float getRating() {
        return rating;
    }

    public float getReviewer() {
        return reviewer;
    }

    public String getEmail() {
        return email;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isType() {
        return type;
    }

    public String getExpert() {
        return expert;
    }

}
