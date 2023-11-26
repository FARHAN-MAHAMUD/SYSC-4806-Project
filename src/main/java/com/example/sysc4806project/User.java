package com.example.sysc4806project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user1")
public class User {

    @Id
    @GeneratedValue
    private long user_id;

    private String name = "Default";

    private String username;
    private String password;

    @ElementCollection
    private Map<Book, Integer> shoppingCart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistory;

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
        this.shoppingCart = new HashMap<>();
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
    public Map<Book, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public void addBookToCart(Book book, int quantity){
        int newAmount = quantity;

        if (this.shoppingCart.containsKey(book)){
            newAmount += this.shoppingCart.get(book);
        }

        this.shoppingCart.put(book, newAmount);
    }

    public int removeBookFromCart(Book book, int quantity){
        int newAmount = 0;

        if (this.shoppingCart.containsKey(book)){
            int currentAmount = this.shoppingCart.get(book);

            if (currentAmount < quantity){
                this.shoppingCart.remove(book);
                return currentAmount;
            } else {
                newAmount = currentAmount - quantity;
                this.shoppingCart.put(book, newAmount);
                return quantity;
            }
        } else {
            return 0;
        }

    }

    /**
     * Getter for the ID attribute
     * @return ID attribute
     */
    public long getUser_id() {
        return user_id;
    }
}
