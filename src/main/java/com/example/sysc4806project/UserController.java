package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private final BookstoreRepository bookstoreRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookstoreController bookstoreController;

    /**
     * Constructor for the UserController class
     * @param userRepository user repository attribute
     * @param bookstoreRepository bookstore repository attribute
     * @param bookstoreController bookstore controller attribute
     */
    @Autowired
    public UserController(UserRepository userRepository, BookstoreRepository bookstoreRepository, BookstoreController bookstoreController) {
        this.userRepository = userRepository;
        this.bookstoreRepository = bookstoreRepository;
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
                price += (entry.getKey().getPrice() * entry.getValue());
                //bookstoreController.setBook(entry.getValue(), ); Not done yet to remove book from bookstore after checkout
            }
        } catch(NullPointerException nullPointerException){
            System.out.println("User not found or cart is empty!");
        }
        user.getShoppingCart().clear();
        return price;
     }
}
