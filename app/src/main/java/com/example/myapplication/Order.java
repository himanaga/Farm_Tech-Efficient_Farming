package com.example.myapplication;

import java.util.List;

public class Order {
    private String userId;
    private String phoneNumber;
    private String address;
    private List<Farm> items;

        // constructor, getters, and setters
    public Order(){

        }


    public Order(String userId, String phoneNumber, String address, List<Farm> items) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Farm> getItems() {
        return items;
    }

    public void setItems(List<Farm> items) {
        this.items = items;
    }
}
