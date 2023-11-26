package com.example.sysc4806project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;//

import static org.junit.jupiter.api.Assertions.*;//

public class BookstoreTest {
    @Mock
    private BookstoreRepository bookRepository;//

    @Mock
    private UserRepository userRepository;//

    private BookstoreController bookstore;//

    @BeforeEach
    public void setUp() throws Exception {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            bookstore = new BookstoreController(bookRepository, userRepository);
        }
    }//

    @Test
    public void testSetBook() {
        // Mock behavior for repositories
        Book existingBook = new Book("Title", "Author", 1234567890L, 10f, 10);
        Mockito.when(bookRepository.findByISBN(existingBook.getISBN())).thenReturn(existingBook);

        // Test adding a new book
        Book newBook = new Book("New Title", "New Author", 9876543210L, 10f, 10);
        bookstore.setBook(newBook);//

        // Verify that the book and inventory repositories were called as expected
        Mockito.verify(bookRepository).save(newBook);

    }//

    @Test
    public void testPurchaseBook() {
        // Mock behavior for repositories
        Book book = new Book("Title", "Author", 1234567890L, 10f, 10);
        bookstore.setBook(book);

        Mockito.when(bookRepository.findByISBN(book.getISBN())).thenReturn(book);//

        // Verify that the inventory is updated and the purchase is successful
        assertEquals(50f, bookstore.purchaseBook(5, book));//

        // Verify that the quantity is appropriately modified after a successful purchase
        assertEquals(5, book.getQuantity());//

        // Verify that the quantity remains unchanged after a failed purchase
        assertEquals(5, bookRepository.findByISBN(book.getISBN()).getQuantity());//
    }//

    @Test
    public void testRemoveBook_SuccessfulRemoval() {
        // Mock behavior for repositories
        Book book = new Book("Title", "Author", 1234567890L, 10f, 10);
        bookstore.setBook(book);

        Mockito.when(bookRepository.findByISBN(book.getISBN())).thenReturn(book);//

        // Test removing a book that exists in the inventory
        bookstore.removeBook(book.getISBN());//

        // Verify that the inventory entry is deleted, and the removal is successful
        assertFalse(bookRepository.existsByISBN(book.getISBN()));
    }//
}//
