package com.example.sysc4806project;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Book {

    private String title;
    private String author;

    @Id
    private final long ISBN;
    private float price;
    private int quantity;

    public Book() {
        ISBN = 1; //TODO: I just added this to get rid of compilation errors in Inventory
    }

    public Book(String title, String author, long ISBN, float price, int quantity) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.price = price;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getISBN() {
        return ISBN;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
