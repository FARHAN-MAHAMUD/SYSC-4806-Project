package com.example.sysc4806project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class RecommendationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRecommendations() throws Exception {
        // Set up mock data or behavior for userRepository and purchaseHistoryRepository
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

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
                createPurchaseHistory(user2, book2),
                createPurchaseHistory(user2, book3)
        );

        List<PurchaseHistory> purchaseHistoryUser3 = Arrays.asList(
                createPurchaseHistory(user3, book1),
                createPurchaseHistory(user3, book2)
        );

        // Mock UserRepository findAll method
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2, user3));

        // Mock UserRepository findById method
        when(userRepository.findById(1L)).thenReturn(user1);
        when(userRepository.findById(2L)).thenReturn(user2);
        when(userRepository.findById(3L)).thenReturn(user3);

        // Mock PurchaseHistoryRepository findByUser method
        when(purchaseHistoryRepository.findByUser(user1)).thenReturn(purchaseHistoryUser1);
        when(purchaseHistoryRepository.findByUser(user2)).thenReturn(purchaseHistoryUser2);
        when(purchaseHistoryRepository.findByUser(user3)).thenReturn(purchaseHistoryUser3);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(
                "/recommendations?userId=1",
                HttpMethod.GET,
                null,
                String.class
        );

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        System.out.println("Response: " + responseEntity.getBody());
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
