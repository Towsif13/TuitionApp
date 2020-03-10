package com.example.tuitionapp;

import android.util.Log;

import androidx.annotation.NonNull;


public class Post {
    private String id, ProfileImage ,FirstName,LastName ,Address , sClass, Date , Time, Days, Region ,Salary, PreferredGender ,Subjects, Notes , Medium;
    private String Department , PreferredMedium, Year;

    // creating model class for recylerView
    public Post() {
    }

    public String getId() {

        Log.d("Post","ji"+ id);
        return id;

    }

    
    public void setId(String id) {
        this.id = id;
    }

    public Post(String Id, String ProfileImage, String Department , String Year, String PreferredMedium, String FirstName, String LastName, String address, String sClass, String date, String time, String days, String region, String salary, String gender, String subjects, String notes, String medium) {

        this.id = Id;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Address = address;
        this.sClass = sClass;
        this.Date = date;
        this.Time = time;
        this.Days = days;
        this.Region = region;
        this.Salary = salary;
        this.PreferredGender = gender;
        this.Subjects = subjects;
        this.Notes = notes;
        this.Medium = medium;
        this.Department = Department;
        this.PreferredMedium = PreferredMedium;
        this.Year = Year;
        this.ProfileImage = ProfileImage;
    }


    public Post( String Id, String FirstName, String ProfileImage,String LastName, String address, String sClass, String date, String time, String days, String region, String salary, String gender, String subjects, String notes, String medium) {

        this.id = Id;
        this.FirstName = FirstName;
        this.LastName = LastName;

        this.Address = address;
        this.sClass = sClass;
        this.Date = date;
        this.Time = time;
        this.Days = days;
        this.Region = region;
        this.Salary = salary;
        this.PreferredGender = gender;
        this.Subjects = subjects;
        this.Notes = notes;
        this.Medium = medium;
        this.ProfileImage = ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }
    public String getProfileImage() {
        return ProfileImage;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getDepartment(){ return Department;};
    public String getPreferredMedium(){return PreferredMedium;}
    public String getYear(){return Year;}
    public String getLastName() {
        return LastName;
    }

    public String getAddress() {
        return Address;
    }

    @NonNull
    public String getsClass() {
        return sClass;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getDays() {
        return Days;
    }

    public String getRegion() {
        return Region;
    }

    public String getSalary() {
        return Salary;
    }

    public String getPreferredGender() {
        return PreferredGender;
    }

    public String getSubjects() {
        return Subjects;
    }

    public String getNotes() {
        return Notes;
    }

    public String getMedium() {
        return Medium;
    }
}
