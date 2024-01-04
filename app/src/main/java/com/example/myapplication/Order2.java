package com.example.myapplication;

import java.util.List;

public class Order2 {
    private String title;
    private double price;
    private Long numberInCart;
    private String imagePath;

    public Order2() {

    }

    public Order2(String imagePath, String title, double price, long numberInCart) {
        this.imagePath = imagePath;
        this.title = title;
        this.price = price;
        this.numberInCart = numberInCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(Long numberInCart) {
        this.numberInCart = numberInCart;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}