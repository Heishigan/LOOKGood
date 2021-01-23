package com.example.lookgood.Model;

public class Products {
        private  String name;
        private String price;
        private boolean isAvailable;
        private String imageURL;
        private String productDescription;



    public Products(String name, String price, boolean isAvailable, String imageURL, String productDescription) {
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.imageURL = imageURL;
        this.productDescription=productDescription;
    }

    public Products()
        {

        }
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
