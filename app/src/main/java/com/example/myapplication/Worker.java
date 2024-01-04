package com.example.myapplication;
import java.io.Serializable;
public class Worker implements Serializable {
    private String Name;
    private String ImagePath;
    private String Skill;
    private double Price;
    private double Num;

    public Worker() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getNum() {
        return Num;
    }

    public void setNum(double num) {
        Num = num;
    }
}

