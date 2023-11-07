package com.example.sysc4806project;

//TODO: entity
public class Book {

    private String title;
    private String author;
    private final long ISBN;

    public Book(String title, String author, long ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
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
}
