package com.example.sysc4806project;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Book class contains all the info a book has including the number of copies stored.
 */
@Entity
public class Book {

    // A string attribute for the title of the book
    private String title;

    // A string attribute for the author of the book
    private String author;

    // A long attribute for the author of the book
    private long ISBN;

    // A float attribute for the price of the book
    private float price;

    // An integer attribute for the quantity of the book
    private int quantity;

    /**
     * The default constructor
     */
    public Book() {
    }

    /**
     * Book constructor
     * @param title String, the title of the book
     * @param author String, the author's name
     * @param ISBN long, the ISBN assigned to a book
     * @param price float, the price of a book
     * @param quantity int, the number of copies of this particular book
     */
    public Book(String title, String author, long ISBN, float price, int quantity) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Get method returns the title of the book.
     * @return String, the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set method changes the title of the book.
     * @param title String, the book's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get method returns the author of the book.
     * @return String, the book's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set method changes the author of a book
     * @param author String, the book's author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get method returns the ISBN of the book.
     * @return String, the book's ISBN
     */
    @Id
    public long getISBN() {
        return ISBN;
    }

    /**
     * Set method sets the book's ISBN.
     * @param ISBN long, the book's ISBN
     */
    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Get method returns the price of the book.
     * @return String, the book's price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Set method changes the price of a book
     * @param price float, the price of a book in dollars & cents
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Get method returns the number of copies of the book.
     * @return String, the book's quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Set method changes the number of copies of a book.
     * @param quantity int, the book's quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * This method is used to print out all the information about a book. Used within the html templates.
     * @return String, a string representation of the contents of a book
     */
    @Override
    public String toString() {
        String results = String.format("Title: %s | Author: %s | ISBN: %d | Price: %.2f | Quantity available: %d", title, author, ISBN, price, quantity);
        //openBook();
        return results;
    }
}