package com.example.sysc4806project;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@Entity
@Table(name = "user1")
public class User {

    @Id
    @GeneratedValue
    private long id;

    private String name = "Default";

    @ElementCollection
    private HashMap<Book, Integer> shoppingCart = new HashMap<>();

    private final boolean ownerStatus;

    /**
     * Default constructor for User
     */
    public User() {
        ownerStatus = false;
    }

    /**
     * Constructor for User
     * @param name user name
     * @param ownerStatus Owner status allowing adding new books to the bookstore
     */
    public User(String name, boolean ownerStatus) {
        this.name = name;
        this.ownerStatus = ownerStatus;
    }


    /**
     * Getter for name
     * @return name attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * @param name name attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for ID
     * @param id new id to be set
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * Setter for the shopping cart
     * @param shoppingCart shopping cart to be set
     */
    public void setShoppingCart(HashMap<Book, Integer> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * Getter for the Shopping Cart
     * @return shopping cart atttribute
     */
    public HashMap<Book, Integer> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Getter for the ID attribute
     * @return ID attribute
     */
    public long getId() {
        return id;
    }
}
