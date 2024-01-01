package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(Farm item) {
        ArrayList<Farm> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listpop.add(item);
        }
        tinyDB.putListObject("CartList", listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Farm> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public Double getTotalFee() {
        ArrayList<Farm> listItem = getListCart();
        double fee = 0;
        for (int i = 0; i < listItem.size(); i++) {
            fee = fee + (listItem.get(i).getPrice() * listItem.get(i).getNumberInCart());
        }
        return fee;
    }

    public void minusNumberItem(ArrayList<Farm> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (listItem.get(position).getNumberInCart() == 1) {
            listItem.remove(position);
        } else {
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() - 1);
        }
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<Farm> listItem, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    // Clear the cart
    public void clearCart() {
        tinyDB.putListObject("CartList", new ArrayList<>());
    }

    public List<Order> getCartItems() {
        // Retrieve the cart items from TinyDB
        ArrayList<Farm> listItem = getListCart();

        // Convert Farm objects to orders objects
        List<Order> cartItems = new ArrayList<>();

        cartItems.add(new Order(/*Farm.getTitle(), Farm.getPrice(), Farm.getNumberInCart()*/"defaultUserId", "defaultPhoneNumber", "defaultAddress",getListCart()));


        return cartItems;

    }
}