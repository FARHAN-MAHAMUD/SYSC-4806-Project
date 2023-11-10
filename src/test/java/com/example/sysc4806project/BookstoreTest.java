package com.example.sysc4806project;

//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;//

//import static org.junit.jupiter.api.Assertions.*;//

//public class BookstoreTest {
//    @Mock
//    private BookstoreRepository bookRepository;//

//    @Mock
//    private InventoryRepository inventoryRepository;//

//    private BookstoreController bookstore;//

//    @BeforeEach
//    public void setUp() throws Exception {
//        try (var mocks = MockitoAnnotations.openMocks(this)) {
//            bookstore = new BookstoreController(bookRepository, inventoryRepository);
//        }
//    }//

//    @Test
//    public void testSetBook() {
//        // Mock behavior for repositories
//        Book existingBook = new Book("Title", "Author", 1234567890L);
//        Mockito.when(bookRepository.findByISBN(existingBook.getISBN())).thenReturn(existingBook);
//        Mockito.when(inventoryRepository.findByBook(existingBook)).thenReturn(null);//

//        // Test adding a new book
//        Book newBook = new Book("New Title", "New Author", 9876543210L);
//        bookstore.setBook(newBook, 10);//

//        // Verify that the book and inventory repositories were called as expected
//        Mockito.verify(bookRepository).save(newBook);
//        Mockito.verify(inventoryRepository).save(Mockito.any(Inventory.class));
//    }//

//    @Test
//    public void testPurchaseBook() {
//        // Mock behavior for repositories
//        Book book = new Book("Title", "Author", 1234567890L);
//        Inventory inventory = new Inventory();
//        inventory.setBook(book);
//        inventory.setQuantity(10);//

//        Mockito.when(inventoryRepository.findByBook(book)).thenReturn(inventory);//

//        // Test purchasing a book with sufficient inventory
//        boolean purchaseResult = bookstore.purchaseBook(book, 5);//

//        // Verify that the inventory is updated and the purchase is successful
//        assertTrue(purchaseResult);//

//        // Verify that the quantity is appropriately modified after a successful purchase
//        assertEquals(5, inventory.getQuantity());//

//        // Test purchasing a book with insufficient inventory
//        purchaseResult = bookstore.purchaseBook(book, 20);//

//        // Verify that the inventory is not updated and the purchase fails
//        assertFalse(purchaseResult);//

//        // Verify that the quantity remains unchanged after a failed purchase
//        assertEquals(5, inventory.getQuantity());//

//        // Verify that inventoryRepository.save was called the appropriate number of times (once per successful purchase)
//        Mockito.verify(inventoryRepository, Mockito.times(1)).save(inventory);
//    }//

//    @Test
//    public void testRemoveBook_SuccessfulRemoval() {
//        // Mock behavior for repositories
//        Book book = new Book("Title", "Author", 1234567890L);
//        Inventory inventory = new Inventory();
//        inventory.setBook(book);
//        inventory.setQuantity(10);//

//        Mockito.when(inventoryRepository.findByBook(book)).thenReturn(inventory);//

//        // Test removing a book that exists in the inventory
//        boolean removeResult = bookstore.removeBook(book);//

//        // Verify that the inventory entry is deleted, and the removal is successful
//        Mockito.verify(inventoryRepository).delete(inventory);
//        assertTrue(removeResult);
//    }//

//    @Test
//    public void testRemoveBook_UnsuccessfulRemoval() {
//        // Mock behavior for repositories
//        Book nonExistingBook = new Book("Non-Existing Title", "Non-Existing Author", 9876543210L);
//        Mockito.when(inventoryRepository.findByBook(nonExistingBook)).thenReturn(null);//

//        // Test removing a book that doesn't exist in the inventory
//        boolean removeNonExistingResult = bookstore.removeBook(nonExistingBook);//

//        // Verify that the removal fails as the book is not found in the inventory
//        Mockito.verify(inventoryRepository, Mockito.never()).delete(Mockito.any(Inventory.class));
//        assertFalse(removeNonExistingResult);
//    }//

//}//
