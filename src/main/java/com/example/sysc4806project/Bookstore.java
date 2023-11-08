package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class Bookstore {

    private final BookRepository bookRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public Bookstore(BookRepository bookRepository, InventoryRepository inventoryRepository) {
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Add a book to the bookstore's inventory or update its quantity.
     *
     * @param book     The book to add or update.
     * @param quantity The quantity to add to the inventory.
     */
    public void setBook(Book book, int quantity) {
        // Check if the book already exists in the database
        Book existingBook = bookRepository.findByISBN(book.getISBN());

        if (existingBook == null) {
            // If the book doesn't exist, save it in the Book table
            bookRepository.save(book);
        } else {
            // If the book already exists, update it
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            bookRepository.save(existingBook);
        }

        // Add or update the book's quantity in the Inventory table
        Inventory inventory = inventoryRepository.findByBook(existingBook);
        if (inventory == null) {
            inventory = new Inventory();
            inventory.setBook(existingBook);
            inventory.setQuantity(quantity);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        }
        inventoryRepository.save(inventory);
    }

    /**
     * Attempt to purchase a specified quantity of a book from the bookstore's inventory.
     *
     * @param book     The book to purchase.
     * @param quantity The quantity of the book to purchase.
     * @return True if the purchase was successful, false if the book is not in stock or the requested quantity exceeds the available quantity.
     */
    public boolean purchaseBook(Book book, int quantity) {
        // Check if the book is in stock
        Inventory inventory = inventoryRepository.findByBook(book);
        if (inventory != null && inventory.getQuantity() >= quantity) {
            // Update the inventory
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryRepository.save(inventory);

            // TODO: Perform the purchase
            // I'm assuming we'll have some table about purchase history so that will be implemented later
            // Customer will add books to cart in the GUI, and when they click buy,
            // for each book in the cart, it'll make an API call to our controller,
            // which will call this, and check if it was successful

            return true;
        }

        return false;
    }

    /**
     * Remove a book from the bookstore's inventory.
     *
     * @param book The book to remove.
     * @return True if the book was successfully removed, false if the book is not found in the inventory.
     */
    public boolean removeBook(Book book) {
        Inventory inventory = inventoryRepository.findByBook(book);
        if (inventory != null) {
            inventoryRepository.delete(inventory);
            return true;
        }
        return false;
    }



}
