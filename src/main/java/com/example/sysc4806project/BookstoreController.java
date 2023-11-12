package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookstoreController {

    private final BookstoreRepository bookstoreRepository;

    @Autowired
    public BookstoreController(BookstoreRepository bookstoreRepository) {
        this.bookstoreRepository = bookstoreRepository;
    }

    /**
     * Opens the owner html page view.
     * 
     * @param name      The name of the current user navigating the page.
     * @param model     The data model of the bookstore system.
     */
    @GetMapping("/owner")
    public String ownerView(@RequestParam(name="name", required=false, defaultValue="Jacob") String name, Model model) {
        StringBuilder str = new StringBuilder();
        bookstoreRepository.findAll().forEach(book -> str.append(book.toString() + "\n"));

        model.addAttribute("name", name);
        model.addAttribute("books", str.toString());
        return "owner";
    }

    /**
     * Opens the customer html page view.
     * 
     * @param name      The name of the current user navigating the page.
     * @param model     The data model of the bookstore system.
     */
    @GetMapping("/customer")
    public String customerView(@RequestParam(name="name", required=false, defaultValue="Jacob") String name, Model model) {
        StringBuilder str = new StringBuilder();
        bookstoreRepository.findAll().forEach(book -> str.append(book.toString() + "\n"));

        model.addAttribute("name", name);
        model.addAttribute("books", str.toString());
        return "customer";
    }
    /**
     * Add a book to the bookstore's inventory or update its quantity.
     *
     * @param book     The book to add or update.
     */
    @PostMapping("/owner")
    public String setBook(@RequestBody Book book) {
        // Check if the book already exists in the database
        boolean bookExists = bookstoreRepository.existsByISBN(book.getISBN());

        if (!bookExists) {
            // If the book doesn't exist, save it in the Book table
            bookstoreRepository.save(book);
        } else {
            // If the book already exists, update it
            Book existingBook = bookstoreRepository.findByISBN(book.getISBN());
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setPrice(book.getPrice());
            existingBook.setQuantity(existingBook.getQuantity() + book.getQuantity());
            bookstoreRepository.save(existingBook);
        }

        return "owner";
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
     * @param isbn The isbn of the book to remove.
     * @return True if the book was successfully removed, false if the book is not found in the inventory.
     */
    @DeleteMapping("/owner")
    public String removeBook(@RequestParam("isbn") long isbn) {
        // Check if the book already exists in the database
        boolean bookExists = bookstoreRepository.existsByISBN(isbn);

        if (bookExists) {
            Book existingBook = bookstoreRepository.findByISBN(isbn);
            bookstoreRepository.delete(existingBook);
        }
        return "owner";
    }
}
