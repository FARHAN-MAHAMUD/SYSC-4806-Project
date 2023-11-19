package com.example.sysc4806project;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PurchaseHistory {

    @Id
    @GeneratedValue
    private Long purchase_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ISBN")
    private Book book;

    private LocalDateTime purchaseDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBookId(Book book) {
        this.book = book;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
