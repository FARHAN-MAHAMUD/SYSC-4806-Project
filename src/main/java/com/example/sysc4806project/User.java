package com.example.sysc4806project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "user1")
public class User {

    @Id
    @GeneratedValue
    private long user_id;

    private String name = "Default";

    @ElementCollection
    private HashMap<Book, Integer> shoppingCart = new HashMap<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistory = new ArrayList<>();

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
    public void setUser_id(long id) {
        this.user_id = id;
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
    public long getUser_id() {
        return user_id;
    }
}
