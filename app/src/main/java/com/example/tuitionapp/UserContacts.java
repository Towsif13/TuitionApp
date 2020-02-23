
package com.example.tuitionapp;

import android.util.Log;

public class UserContacts {

    private String id,FirstName,LastName,Address;//tutors_image;

    public String getId() {
        Log.d("UserContacts","Id"+ id);
        return id;
    }

    public UserContacts(String id, String FirstName, String LastName, String Address, String status )
/* String tutors_image*/
{
        this.id = id;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Address = Address;
       // this.tutors_image = tutors_image;

    }

    public UserContacts(){

    }


    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return Address;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    /* public String getTutors_image() {
        return tutors_image;
    }*/


    public void setAddress(String address) {
        this.Address = address;
    }

    public void setFirstName(String first_Name) {
        this.FirstName = first_Name;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    /*  public void setTutors_image(String tutors_image) {
        this.tutors_image = tutors_image;
    }*/

}
