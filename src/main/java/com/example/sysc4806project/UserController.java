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

    @Autowired
    public UserController(UserRepository userRepository, BookstoreRepository bookstoreRepository, BookstoreController bookstoreController) {
        this.userRepository = userRepository;
        this.bookstoreRepository = bookstoreRepository;
        this.bookstoreController = bookstoreController;
    }


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
