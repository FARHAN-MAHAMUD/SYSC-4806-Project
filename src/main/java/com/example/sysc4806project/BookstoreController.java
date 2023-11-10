package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class BookstoreController {

    private final BookstoreRepository bookstoreRepository;

    @Autowired
    public BookstoreController(BookstoreRepository bookstoreRepository) {
        this.bookstoreRepository = bookstoreRepository;
    }

    public String homeView(Model model) {
        return null;
    }

    public String ownerView(Model model) {
        return null;
    }

    public String customerView(Model model) {
        return null;
    }

    /**
     * Add a book to the bookstore's inventory or update its quantity.
     *
     * @param book     The book to add or update.
     * @param quantity The quantity to add to the inventory.
     */
    public String setBook(@RequestParam("quantity") int quantity, @RequestBody Book book) {
        // Check if the book already exists in the database
        Book existingBook = bookstoreRepository.findByISBN(book.getISBN());

        if (existingBook == null) {
            // If the book doesn't exist, save it in the Book table
            book.setQuantity(1);
            bookstoreRepository.save(book);
        } else {
            // If the book already exists, update it
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setQuantity(existingBook.getQuantity() + 1);
            bookstoreRepository.save(existingBook);
        }

        return null;
    }

    /**
     * Attempt to purchase a specified quantity of a book from the bookstore's inventory.
     *
     * @param book     The book to purchase.
     * @param quantity The quantity of the book to purchase.
     * @return True if the purchase was successful, false if the book is not in stock or the requested quantity exceeds the available quantity.
     */
    public String purchaseBook(@RequestParam("quantity") int quantity, @RequestBody Book book) {
        // Check if the book is in stock
        Book existingBook = bookstoreRepository.findByISBN(book.getISBN());
        if (existingBook != null && existingBook.getQuantity() >= quantity) {
            // Update the inventory
            existingBook.setQuantity(existingBook.getQuantity() - quantity);
            bookstoreRepository.save(existingBook);

            // TODO: Perform the purchase
            // I'm assuming we'll have some table about purchase history so that will be implemented later
            // Customer will add books to cart in the GUI, and when they click buy,
            // for each book in the cart, it'll make an API call to our controller,
            // which will call this, and check if it was successful
        }

        return null;
    }

    /**
     * Remove a book from the bookstore's inventory.
     *
     * @param book The book to remove.
     * @return True if the book was successfully removed, false if the book is not found in the inventory.
     */
    public String removeBook(@RequestBody Book book) {
        // Check if the book already exists in the database
        Book existingBook = bookstoreRepository.findByISBN(book.getISBN());

        if (existingBook != null) {
            bookstoreRepository.delete(existingBook);
        }
        return null;
    }
}
