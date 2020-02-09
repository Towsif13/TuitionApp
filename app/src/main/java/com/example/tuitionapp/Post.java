package com.example.tuitionapp;

import android.graphics.Region;
import android.location.Address;

import androidx.annotation.NonNull;

import java.security.acl.LastOwnerException;

public class Post {
    String  FirstName,LastName ,Address , sClass, Date , Time, Days, Region ,Salary, PreferredGender ,Subjects, Notes , Medium;
    // creating model class for recylerView
    public Post() {
    }

    public Post(String FirstName,String LastName,String address, String sClass, String date, String time, String days, String region, String salary, String gender, String subjects, String notes, String medium) {
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
    }

    public String getFirstName() {
        return FirstName;
    }

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
