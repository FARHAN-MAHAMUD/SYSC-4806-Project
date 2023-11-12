package com.example.sysc4806project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Mock
    private BookstoreRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    private UserController userController;

    private BookstoreController bookstoreController;

    @BeforeEach
    void setUp() {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            bookstoreController = new BookstoreController(bookRepository);
            userController =  new UserController(userRepository, bookRepository, bookstoreController);
        } catch (Exception e) {
            System.out.println("Unable to start tests");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shoppingCartTest() {
        //setting up default parameters
        Book existingBook = new Book("Book", "Author", 123456L, 10F, 5);
        User customer = new User("CustomerTest", false);
        Mockito.when(bookRepository.findByISBN(existingBook.getISBN())).thenReturn(existingBook);
        Mockito.when(userRepository.findById(customer.getId())).thenReturn(customer);

        //try adding to cart
        userController.addItemToCart(customer.getId(), 5, bookRepository.findByISBN(existingBook.getISBN()));
        assertEquals(5, customer.getShoppingCart().get(existingBook));

        //try removing from cart
        userController.removeItemFromCart(customer.getId(), 1, existingBook);
        assertEquals(4, customer.getShoppingCart().get(existingBook));

        //try emptying cart
        assertEquals( 4 * 10F, userController.checkoutUser(customer.getId()));
        assertEquals(0 , customer.getShoppingCart().size());
    }
}