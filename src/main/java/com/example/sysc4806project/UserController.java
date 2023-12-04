package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class UserController {

    private final BookstoreRepository bookstoreRepository;
    private final UserRepository userRepository;
    private final BookstoreController bookstoreController;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    /**
     * Constructor for the UserController class
     * @param userRepository user repository attribute
     * @param bookstoreRepository bookstore repository attribute
     * @param bookstoreController bookstore controller attribute
     */
    @Autowired
    public UserController(UserRepository userRepository, BookstoreRepository bookstoreRepository,
                          PurchaseHistoryRepository purchaseHistoryRepository, BookstoreController bookstoreController) {
        this.userRepository = userRepository;
        this.bookstoreRepository = bookstoreRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.bookstoreController = bookstoreController;
    }


    /**
     * Adds a Book object to a user's cart based on the user's ID
     * @param id user's ID that needs the book added
     * @param quantity quantity of the book to be added
     * @param isbn ISBN of the book wanting to be added
     */
    @PostMapping("/customer/addBookToCart")
    public String addItemToCart(@RequestParam("id") long id, @RequestParam("quantity") int quantity, @RequestParam("isbn") long isbn) {

        if (quantity > 0) {
            User user = userRepository.findById(id);
            Book book = bookstoreRepository.findByISBN(isbn);

            if (quantity > book.getQuantity()) {
                quantity = book.getQuantity();
            }

            user.addBookToCart(book, quantity);
            userRepository.save(user);
        }
        return "customer";
    }

    /**
     * Removes a Book object from a user's cart based on the user's ID
     * @param id user's ID that needs the book removed
     * @param quantity quantity of the book to be removed
     * @param isbn the isbn of the book to be removed
     */
    @DeleteMapping("/customer/removeItemFromCart")
    public String removeItemFromCart(@RequestParam("id") long id, @RequestParam("quantity") int quantity, @RequestParam("isbn") long isbn) {

        if (quantity > 0) {
            User user = userRepository.findById(id);
            Book book = bookstoreRepository.findByISBN(isbn);

            user.removeBookFromCart(book, quantity);
            userRepository.save(user);
        }

        return "customer";
    }

    /**
     * Checks out a user based on the user's ID, emptying his cart and returning a total price
     * @param id user's ID that needs to check out
     * @return final price
     */
    @PostMapping("customer/checkoutUser")
    public String checkoutUser(@RequestParam("id") long id) {

        System.out.println(userRepository.existsById(id));

        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id);
            float price = 0.0F;

            try {
                for (Map.Entry<Book, Integer> entry : user.getShoppingCart().entrySet()) {
                    price += bookstoreController.purchaseBook(entry.getValue(), entry.getKey());

                    // Add a record to the purchase history
                    PurchaseHistory purchaseRecord = new PurchaseHistory();
                    purchaseRecord.setUser(user);
                    purchaseRecord.setBookId(entry.getKey());
                    purchaseRecord.setPurchaseDate(LocalDateTime.now());
                    purchaseHistoryRepository.save(purchaseRecord);
                }
            } catch (NullPointerException nullPointerException) {
                System.out.println("User not found or cart is empty!");
            }
            user.getShoppingCart().clear();
            userRepository.save(user);
        }
//        return price;
        return "customer";
    }

    /**
     * Used to display custom login screen
     */
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
