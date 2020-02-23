package com.example.tuitionapp;

public class Request {

    private String Sent_Name, receive_id,sent_id,request_type;

    Request(){

    }
    public String getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(String receive_id) {
        this.receive_id = receive_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getSent_id() {
        return sent_id;
    }

    public void setSent_id(String sent_id) {
        this.sent_id = sent_id;
    }

    public Request(String sent_Name, String receive_id, String request_type, String sent_id) {
        Sent_Name = sent_Name;
        this.receive_id = receive_id;
        this.request_type = request_type;
        this.sent_id = sent_id;
    }



    public String getSent_Name() {
        return Sent_Name;
    }

    public void setSent_Name(String sent_Name) {
        Sent_Name = sent_Name;
    }
}
