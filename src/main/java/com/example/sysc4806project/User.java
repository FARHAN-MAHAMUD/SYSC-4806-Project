package com.example.sysc4806project;

import java.util.HashMap;

//TODO: entity
public abstract class User {

    private long id;

    private String name;

    private HashMap<Book, Integer> shoppingCart;

    private BookstoreController bookstore;

    public void buy() {
        //TODO
    }

    public void addItemToCart() {
        //TODO
    }

    public void removeItemFromCart() {
        //TODO
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
