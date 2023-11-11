package com.example.sysc4806project;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @ElementCollection
    private HashMap<Book, Integer> shoppingCart;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setShoppingCart(HashMap<Book, Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public HashMap<Book, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public long getId() {
        return id;
    }
}
