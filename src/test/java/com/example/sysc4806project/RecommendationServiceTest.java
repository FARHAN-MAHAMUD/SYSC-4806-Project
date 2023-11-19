package com.example.sysc4806project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @Mock
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateJaccardSimilarity() {
        User user1 = new User();
        User user2 = new User();

        // Create three books with unique ISBNs
        Book book1 = createBook(123, "Book1", "Author1");
        Book book2 = createBook(456, "Book2", "Author2");
        Book book3 = createBook(789, "Book3", "Author3");

        // Use these books in your purchase history lists
        List<PurchaseHistory> purchaseHistoryUser1 = Arrays.asList(
                createPurchaseHistory(user1, book1),
                createPurchaseHistory(user1, book2)
        );

        List<PurchaseHistory> purchaseHistoryUser2 = Arrays.asList(
                createPurchaseHistory(user2, book2),  // Reusing book2
                createPurchaseHistory(user2, book3)
        );

        when(purchaseHistoryRepository.findByUser(user1)).thenReturn(purchaseHistoryUser1);
        when(purchaseHistoryRepository.findByUser(user2)).thenReturn(purchaseHistoryUser2);

        double similarity = recommendationService.calculateJaccardSimilarity(user1, user2);

        // Assuming Jaccard similarity is calculated correctly
        assertEquals(0.33, similarity, 0.01);

        // Verify that repository methods were called
        verify(purchaseHistoryRepository, times(2)).findByUser(any(User.class));
    }

    private Book createBook(long isbn, String title, String author) {
        return new Book(title, author, isbn, 0.0f, 0);
    }

    private PurchaseHistory createPurchaseHistory(User user, Book book) {
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setUser(user);
        purchaseHistory.setBookId(book);
        return purchaseHistory;
    }
}
