package com.example.sari.backend;


/**
 * The object model for the data we are sending through endpoints
 */
public class Printer {

    private String myData = "";

    public String getData() {
        return myData;
    }

    public void setData(String data) {
        myData = data;
    }

    public void addData(String data){myData += " "+data;}
}