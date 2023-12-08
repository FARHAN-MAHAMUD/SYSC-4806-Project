package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for controlling the book store
 */
@Controller
public class BookstoreController {

    private final BookstoreRepository bookstoreRepository;
    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    @Autowired
    public BookstoreController(BookstoreRepository bookstoreRepository, UserRepository userRepository, PurchaseHistoryRepository purchaseHistoryRepository) {
        this.bookstoreRepository = bookstoreRepository;
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    /**
     * Opens the owner html page view.
     *
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
     * @param model The data model of the bookstore system.
     */
    @GetMapping("/customer")
    public String customerView(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        // If the user doesn't exist with that username, create and store in database (not permanent since its H2)
        if (userRepository.findByName(name) == null) {
            System.out.println("DOESNT EXIST YET");
            user = new User(name); // automatically sets a generated id
            userRepository.save(user);
            if (bookstoreRepository.findByISBN(1L) == null) {
                // Default books
                Book book1 = new Book("Lebron James Autobiography", "Subear Jama", 1L, 10, 111);
                Book book2 = new Book("To Save a Hummingbird", "Unknown", 2L, 15, 222);
                Book book3 = new Book("Now I Know My ABCs", "Dr Seuss", 3L, 13,333);
                Book book4 = new Book("2023 Freshman XXL Cipher", "Patrick Vafoie", 4L, 20,444);
                bookstoreRepository.save(book1);
                bookstoreRepository.save(book2);
                bookstoreRepository.save(book3);
                bookstoreRepository.save(book4);
            }
        } else {
            System.out.println("EXISTS");
            user = userRepository.findByName(name);
        }

        StringBuilder storeBooks = new StringBuilder();
        StringBuilder cartBooks = new StringBuilder();

        bookstoreRepository.findAll().forEach(book -> storeBooks.append(book.toString() + "\n"));
        userRepository.findById(user.getUser_id()).getShoppingCart().forEach((book, amount) -> cartBooks.append(book.toString() + " | Amount in cart: " + amount + "\n"));

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
    public void purchaseBook(@RequestParam("quantity") int quantity, @RequestBody Book book) {
        // Check if the book is in stock
        Book existingBook = bookstoreRepository.findByISBN(book.getISBN());
        if (existingBook != null && existingBook.getQuantity() >= quantity) {
            // Update the inventory
            existingBook.setQuantity(existingBook.getQuantity() - quantity);
            bookstoreRepository.save(existingBook);
        }
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
        System.out.println("RemoveBook: " + bookExists);

        if (bookExists) {
            Book existingBook = bookstoreRepository.findByISBN(isbn);
            System.out.println(existingBook);
            for (PurchaseHistory purchaseHistory: purchaseHistoryRepository.findByBook(existingBook)) {
                purchaseHistoryRepository.delete(purchaseHistory);
            }
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
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = (List<Book>) bookstoreRepository.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Gets all of the books in a user's cart
     * @return
     */
    @GetMapping("/getCart")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getCart() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByName(name);
        Map<Book, Integer> shoppingCart = user.getShoppingCart();

        List<Map<String, Object>> cartList = new ArrayList<>();

        for (Map.Entry<Book, Integer> entry : shoppingCart.entrySet()) {
            Map<String, Object> cartItem = new HashMap<>();
            Book book = entry.getKey();

            cartItem.put("Title", book.getTitle());
            cartItem.put("Author", book.getAuthor());
            cartItem.put("ISBN", book.getISBN());
            cartItem.put("Price", book.getPrice());
            cartItem.put("Available", book.getQuantity());
            cartItem.put("Quantity", entry.getValue());

            cartList.add(cartItem);
        }

        return new ResponseEntity<>(cartList, HttpStatus.OK);
    }

}
