package com.example.myapplication;
import java.io.Serializable;

public class User implements Serializable {
    // int id;
     String name;
     String price;
     String description;
     int img;

    public User(String name, String price, String description, int img) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.img = img;
      //  this.id = id;
    }

    // Getter methods if needed
  //  public int getId() {     return id; }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getImg() {
        return img;
    }
}
