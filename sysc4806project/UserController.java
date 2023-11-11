package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void addItemToCart(@RequestParam("id") long id, @RequestParam("quantity") int quantity, @RequestBody Book book) {
        User user = userRepository.findById(id);

        try{
            user.getShoppingCart().put(book, quantity);
        } catch (NullPointerException nullPointerException) {
            System.out.println("User not found!");
        }
    }

    public void removeItemFromCart(@RequestParam("id") long id, @RequestParam("quantity") int quantity, @RequestBody Book book) {
        User user = userRepository.findById(id);

        try{
            user.getShoppingCart().remove(book, quantity);
        } catch (NullPointerException nullPointerException) {
            System.out.println("User not found!");
        }
    }

    public int checkoutUser(@RequestParam("id") long id) {
        User user = userRepository.findById(id);
        int price = 0;

        try {
            for (Map.Entry<Book, Integer> entry: user.getShoppingCart().entrySet()){
                price += (int) (entry.getKey().getPrice() * entry.getValue());
            }
        } catch(NullPointerException nullPointerException){
            System.out.println("User not found or cart is empty!");
        }

        return price;
    }
}
