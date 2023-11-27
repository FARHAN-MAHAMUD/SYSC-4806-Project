package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookstoreController {

    private final BookstoreRepository bookstoreRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookstoreController(BookstoreRepository bookstoreRepository, UserRepository userRepository) {
        this.bookstoreRepository = bookstoreRepository;
        this.userRepository = userRepository;
    }

    /**
     * Opens the owner html page view.
     *
     * @param name  The name of the current user navigating the page.
     * @param model The data model of the bookstore system.
     */
    @GetMapping("/owner")
    public String ownerView(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        StringBuilder str = new StringBuilder();
        bookstoreRepository.findAll().forEach(book -> str.append(book.toString() + "\n"));

        model.addAttribute("name", name);
        model.addAttribute("books", str.toString());
        return "owner";
    }

    /**
     * Opens the customer html page view.
     *
     * @param name  The name of the current user navigating the page.
     * @param model The data model of the bookstore system.
     */
    @GetMapping("/customer")
    public String customerView(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User(name, false);
        StringBuilder storeBooks = new StringBuilder();
        StringBuilder cartBooks = new StringBuilder();

        if(!userRepository.existsById(1L)) {
            Book book1 = new Book("A", "A", 1L, 10, 111);
            bookstoreRepository.save(book1);
            userRepository.save(user);
        }
        bookstoreRepository.findAll().forEach(book -> storeBooks.append(book.toString() + "\n"));
        userRepository.findById(1).getShoppingCart().forEach((book, amount) -> cartBooks.append(book.toString() + " | Amount: " + amount + "\n"));

        model.addAttribute("name", name);
        model.addAttribute("books", storeBooks.toString());
        model.addAttribute("cart", cartBooks.toString());
        model.addAttribute("userID", 1L);
        return "customer";
    }

    /**
     * Add a book to the bookstore's inventory or update its quantity.
     *
     * @param book The book to add or update.
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
    public float purchaseBook(@RequestParam("quantity") int quantity, @RequestBody Book book) {
        // Check if the book is in stock
        Book existingBook = bookstoreRepository.findByISBN(book.getISBN());
        if (existingBook != null && existingBook.getQuantity() >= quantity) {
            // Update the inventory
            existingBook.setQuantity(existingBook.getQuantity() - quantity);
            bookstoreRepository.save(existingBook);
            return quantity * existingBook.getPrice();
        }
        else if (existingBook != null && quantity > existingBook.getQuantity()) {
            float price = existingBook.getPrice() * existingBook.getQuantity();
            removeBook(existingBook.getISBN());
            return price;
        }
        return 0f;
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

    /**
     * A function for an owner to update the properties of a book with the give ISBN
     * @param isbn The isbn of the book
     * @param quantity The new quantity of the book
     * @param author The new author of the book
     * @param title The new title of the book
     * @param price The new price of the book
     * @return
     */
    @PatchMapping("/owner")
    public String updateBook
    (@RequestParam(value = "isbn") long isbn,
     @RequestParam(value = "quantity", required = false, defaultValue = "-1") int quantity,
     @RequestParam(value = "author", required = false, defaultValue = "none") String author,
     @RequestParam(value = "title", required = false, defaultValue = "none") String title,
     @RequestParam(value = "price", required = false, defaultValue = "-1.00") float price) {

        // Check if the book already exists in the database
        if (bookstoreRepository.existsByISBN(isbn)) {
            Book existingBook = bookstoreRepository.findByISBN(isbn);
            if (quantity > 0) {
                existingBook.setQuantity(quantity);
            }

            if (!author.equals("none")) {
                existingBook.setAuthor(author);
            }

            if (!title.equals("none")) {
                existingBook.setTitle(title);
            }

            if (price > 0){
                existingBook.setPrice(price);
            }

            bookstoreRepository.save(existingBook);
        }

        return "owner";
    }

    /**
     * Get all books from the bookstore's inventory.
     *
     * @return The list of books in the inventory.
     */
    @GetMapping("/getBooks")
    @ResponseBody
    public String getBooks() {
        StringBuilder str = new StringBuilder();
        bookstoreRepository.findAll().forEach(book -> str.append(book.toString() + "\n"));
        return str.toString();
    }

    @GetMapping("/getCart")
    @ResponseBody
    public String getCart() {
        StringBuilder str = new StringBuilder();
        userRepository.findById(1).getShoppingCart().forEach((book, amount) -> str.append(book.toString() + " Amount: " + amount + "\n"));
        return str.toString();
    }
}
