package com.example.myapplication;
import java.io.Serializable;

public class Farm implements Serializable {
    private  int CategoryId;
    private  String Description;
     private boolean Farm;
     private int Id;
     private double Price;
     private String ImagePath;
      private  int PriceId;
      private double Star;
      private String Title;
      private int numberInCart;

public  Farm(){

}
    public Farm(String title, double price, int numberInCart, int categoryId, String description, boolean farm, int Id, String imagePath, int priceId, double star) {
        this.Title = title;
        this.Price = price;
        this.numberInCart = numberInCart;
        this.CategoryId = categoryId;
        this.Description = description;
        this.Farm = farm;
        this.Id = Id;
        this.ImagePath = imagePath;
        this.PriceId = priceId;
        this.Star = star;
    }

    public Farm(String title, double price, int numberInCart) {
        this.Title = title;
        this.Price = price;
        this.numberInCart = numberInCart;
    }
    @Override
    public String toString(){
        return Title;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isFarm() {
        return Farm;
    }

    public void setFarm(boolean farm) {
        Farm = farm;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        Id = Id;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getPriceId() {
        return PriceId;
    }

    public void setPriceId(int priceId) {
        PriceId = priceId;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
