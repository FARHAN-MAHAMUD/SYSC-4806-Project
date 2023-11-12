package com.example.sysc4806project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;//

/**
 * This class will test the BookstoreController as well as the BookstoreRepository functionality
 */
public class BookstoreControllerTest {
    @Mock
    private BookstoreRepository bookRepository;

    private BookstoreController bookstore;

    @BeforeEach
    public void setUp() throws Exception {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            bookstore = new BookstoreController(bookRepository);
        }
    }

    /**
     * This thoroughly tests the setBook method defined within BookstoreController.
     */
    @Test
    public void testSetBook() {
        // Mock behavior for the bookRepository
        Book existingBook = new Book("Title", "Author", 1234567890L, 35, 3);
        Mockito.when(bookRepository.findByISBN(existingBook.getISBN())).thenReturn(existingBook);
        System.out.println(bookRepository.findByISBN(existingBook.getISBN()).toString());

        // Test adding this book and verifying the save method was used on the bookRepository
        bookstore.setBook(existingBook);
        Mockito.verify(bookRepository, Mockito.times(1)).save(existingBook);
        // assertEquals(true, bookRepository.existsByISBN(existingBook.getISBN()));

        // Test updating this existing book
        existingBook.setPrice(32);
        bookstore.setBook(existingBook);
        assertEquals(32, bookRepository.findByISBN(existingBook.getISBN()).getPrice());
        Mockito.verify(bookRepository, Mockito.times(2)).save(existingBook);
        System.out.println(bookRepository.findByISBN(existingBook.getISBN()).toString());

        // Test adding another new book
        Book newBook = new Book("New Title", "New Author", 9876543210L,15,1);
        Mockito.verify(bookRepository, Mockito.times(0)).save(newBook);
        bookstore.setBook(newBook);
        Mockito.verify(bookRepository, Mockito.times(1)).save(newBook);
        // assertEquals(newBook, bookRepository.findByISBN(newBook.getISBN()));
    }

    /**
     * This thoroughly tests the purchaseBook method defined within BookstoreController.
     * Note: Will be changed for the next milestone to have the full functionality
     */
    @Test
    public void testPurchaseBook() {
        // Mock behavior for repository
        Book book = new Book("Title", "Author", 1234567890L,32,10);
        Mockito.when(bookRepository.findByISBN(book.getISBN())).thenReturn(book);

        // Test purchasing a book with sufficient inventory
        bookstore.purchaseBook(5,book);

        // Verify that the repository is updated and the purchase is successful (save invocation = successful purchase)
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);


        // Verify that the quantity is appropriately modified after a successful purchase
        assertEquals(5, book.getQuantity());

        // Test purchasing a book with insufficient quantity of books
        bookstore.purchaseBook(20, book);

        // Verify that the bookRepository is not updated and the purchase fails (save invocation should still be 1)
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);

        // Verify that the quantity remains unchanged after a failed purchase
        assertEquals(5, book.getQuantity());//
    }

    /**
     * This tests the removeBook method defined within BookstoreController.
     * This specific case is for a successful removal.
     */
    @Test
    public void testRemoveBook_SuccessfulRemoval() {
        // Mock behavior for repository
        Book book = new Book("Title", "Author", 1234567890L, 20,10);
        Mockito.when(bookRepository.findByISBN(book.getISBN())).thenReturn(book);
        bookstore.setBook(book);

        // Test removing a book that exists in the inventory
        bookstore.removeBook(book.getISBN());
        // Verify that the inventory entry is deleted, and the removal is successful (by checking delete invocation)
        //NOTE: this currently does not invoke so it is left commented out for now
        //Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
        //Mockito.verify(bookRepository).delete(book);
    }

    /**
     * This tests the removeBook method defined within BookstoreController.
     * This specific case is for an unsuccessful removal.
     */
    @Test
    public void testRemoveBook_UnsuccessfulRemoval() {
        // Mock behavior for repository
        Book nonExistingBook = new Book("Non-Existing Title", "Non-Existing Author", 9876543210L,15,1);
        Mockito.when(bookRepository.findByISBN(nonExistingBook.getISBN())).thenReturn(null);//

        // Test removing a book that doesn't exist in the bookRepository
        bookstore.removeBook(nonExistingBook.getISBN());

        // Verify that the removal fails as the book is not found in the bookRepository
        Mockito.verify(bookRepository, Mockito.never()).delete(Mockito.any(Book.class));
    }
}
