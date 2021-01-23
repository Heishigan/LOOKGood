package com.example.lookgood.Model;

import android.net.Uri;

public class Carts {
    private String name, price, imageURL, size, time;

    public Carts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Carts(String name, String price, String imageURL, String size, String time) {
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
        this.size=size;
        this.time=time;


    }

}
