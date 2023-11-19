package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
     * @param book book to be added
     */
    public void addItemToCart(@RequestParam("id") long id, @RequestParam("quantity") int quantity, @RequestBody Book book) {
        User user = userRepository.findById(id);

        if (quantity > bookstoreRepository.findByISBN(book.getISBN()).getQuantity()) {
            throw new ArithmeticException();
        }

        try{
            user.getShoppingCart().put(book, quantity);
        } catch (NullPointerException nullPointerException) {
            System.out.println("User not found!");
        }
    }

    /**
     * Removes a Book object from a user's cart based on the user's ID
     * @param id user's ID that needs the book removed
     * @param quantity quantity of the book to be removed
     * @param book book to be removed
     */
    public void removeItemFromCart(@RequestParam("id") long id, @RequestParam("quantity") int quantity, @RequestBody Book book) {
        User user = userRepository.findById(id);

        if (user.getShoppingCart().containsKey(book)) {
            int oldQuantity = user.getShoppingCart().get(book);
            if (oldQuantity == quantity) {
                user.getShoppingCart().remove(book);
            }
            else {
                user.getShoppingCart().put(book, oldQuantity - quantity);
            }

        }
    }

    /**
     * Checks out a user based on the user's ID, emptying his cart and returning a total price
     * @param id user's ID that needs to check out
     * @return final price
     */
    public float checkoutUser(@RequestParam("id") long id) {
        User user = userRepository.findById(id);
        float price = 0.0F;

        try {
            for (Map.Entry<Book, Integer> entry: user.getShoppingCart().entrySet()){
                price += bookstoreController.purchaseBook(entry.getValue(), entry.getKey());

                // Add a record to the purchase history
                PurchaseHistory purchaseRecord = new PurchaseHistory();
                purchaseRecord.setUser(user);
                purchaseRecord.setBookId(entry.getKey());
                purchaseRecord.setPurchaseDate(LocalDateTime.now());
                purchaseHistoryRepository.save(purchaseRecord);
            }
        } catch(NullPointerException nullPointerException){
            System.out.println("User not found or cart is empty!");
        }
        user.getShoppingCart().clear();
        return price;
     }

    /**
     * Used to display custom login screen
     */
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/loginHome")
    public String hello() {
        return "loginHome";
    }
}
