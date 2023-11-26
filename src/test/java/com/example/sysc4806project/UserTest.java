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

    @Mock
    private PurchaseHistoryRepository purchaseHistoryRepository;

    private UserController userController;

    private BookstoreController bookstoreController;

    @BeforeEach
    void setUp() {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            bookstoreController = new BookstoreController(bookRepository, userRepository);
            userController =  new UserController(userRepository, bookRepository, purchaseHistoryRepository, bookstoreController);
        } catch (Exception e) {
            System.out.println("Unable to start tests");
        }
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void shoppingCartTest() {
//        //setting up default parameters
//        Book existingBook = new Book("Book", "Author", 123456L, 10F, 5);
//        User customer = new User("CustomerTest", false);
//        Mockito.when(bookRepository.findByISBN(existingBook.getISBN())).thenReturn(existingBook);
//        Mockito.when(userRepository.findById(customer.getUser_id())).thenReturn(customer);
//
//        //try adding to cart
//        userController.addItemToCart(customer.getUser_id(), 5, existingBook.getISBN());
//        assertEquals(5, customer.getShoppingCart().get(existingBook));
//
//        //try removing from cart
//        userController.removeItemFromCart(customer.getUser_id(), 1, existingBook.getISBN());
//        assertEquals(4, customer.getShoppingCart().get(existingBook));
//
//        // Try emptying cart and checking out
//        float totalPrice = userController.checkoutUser(customer.getUser_id());
//        assertEquals(4 * 10F, totalPrice);
//        assertEquals(0, customer.getShoppingCart().size());
//        assertFalse(bookRepository.existsByISBN(existingBook.getISBN())); // Makes sure that the book is removed after being sold
//
//        // Verify that purchase history is created
//        Mockito.verify(purchaseHistoryRepository, Mockito.times(1)).save(Mockito.any(PurchaseHistory.class));
//    }
}