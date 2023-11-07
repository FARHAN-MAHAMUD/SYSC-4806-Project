package com.example.sysc4806project;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Bookstore {

    private String name;
    private long id;
    private HashMap<Book, Integer> books;

    public void setBooks(HashMap<Book, Integer> books) {
        //TODO
    }

    public int getQuantity(Book book) {
        //TODO
        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
