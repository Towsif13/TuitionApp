
package com.example.tuitionapp;

public class UserContacts {

    private String Id,FirstName,LastName,Address,status;//tutors_image;


    public UserContacts(String id, String FirstName, String LastName, String Address, String status )
/* String tutors_image*/
{
        this.Id = id;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Address = Address;
        this.status = status;
       // this.tutors_image = tutors_image;

    }

    public UserContacts(){

    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
