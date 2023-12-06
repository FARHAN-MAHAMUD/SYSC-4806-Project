package com.example.sysc4806project;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * A class to store the purchase history
 */
@Entity
public class PurchaseHistory {

    // A long attribute for the purchase ID
    @Id
    @GeneratedValue
    private Long purchase_id;

    // A User attribute to attach a user to their purchase history
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // A Book attribute to attach a book to a purchase history
    @ManyToOne
    @JoinColumn(name = "ISBN")
    private Book book;

    // A Time attribute to denote the data and time of purchase
    private LocalDateTime purchaseDate;

    /**
     * Gets the user of the history
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user of the history
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the book of the purchase
     * @return
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book for the purchase
     * @param book
     */
    public void setBookId(Book book) {
        this.book = book;
    }

    /**
     * Gets the local time for when the purchase happened
     * @return
     */
    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the time for when the purchase happened
     * @param purchaseDate
     */
    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
