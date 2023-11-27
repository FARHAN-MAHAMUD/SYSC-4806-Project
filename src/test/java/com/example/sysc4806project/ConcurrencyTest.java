package com.example.sysc4806project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import java.util.concurrent.ExecutorService;


import java.util.concurrent.CountDownLatch; //used for concurrent testing
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

/**
 * Concurrency Class used to test the functionality of 2 users (owner & user) accessing the system simultaneously.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrencyTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        // Add both users to InMemoryUserDetailsManager before tests
        UserDetails ownerUser = User.withUsername("owner")
                .password("{noop}ownerpass") // not hashed just plaintext
                .roles("OWNER")
                .build();

        UserDetails userUser = User.withUsername("user")
                .password("{noop}userpass")
                .roles("USER")
                .build();
        inMemoryUserDetailsManager.createUser(ownerUser);
        inMemoryUserDetailsManager.createUser(userUser);
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        int numThreads = 2;
        CountDownLatch latch = new CountDownLatch(numThreads);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int ownerOrUser = i;
            executorService.execute(() -> {
                try {
                    // Owner Thread's requests
                    if (ownerOrUser % 2 == 0) {
                        makePostRequestWithOwner();
                    } else {
                        // User Thread's requests
                        makePostRequestWithUser();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }

    private String makeGetRequestWithOwner() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBasicAuth("owner", "ownerpass");
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/getBooks",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        // Response Asserted
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        //System.out.println(response.getBody());
        return response.getBody();
    }
    private String makeGetRequestWithUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBasicAuth("user", "userpass");
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/getCart",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        // Response Asserted
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        System.out.println(response.getBody());
        return response.getBody();
    }


    private void makePostRequestWithOwner() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBasicAuth("owner", "ownerpass");

        // Set a book for the request body
        Book book = new Book("To Kill a Mockingbird", "Harper Lee", 1, 20, 5);
        HttpEntity<Book> requestEntity = new HttpEntity<>(book, headers);

        System.out.println("OWNER BEFORE...");
        System.out.println(makeGetRequestWithOwner()); //doesn't have book

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/owner",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        // Response Asserted
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        System.out.println("OWNER AFTER...");
        System.out.println(makeGetRequestWithOwner());
        assertThat(makeGetRequestWithOwner()).contains("To Kill a Mockingbird");
    }


    /**
     * Put something in shopping cart
     */
    private void makePostRequestWithUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.setBasicAuth("user", "userpass");
        String requestBody = "{ \"isbn\": \"1\", \"quantity\": 2 }";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        System.out.println("USER BEFORE...");
        //System.out.println(makeGetRequestWithUser()); //doesn't have anything in cart

        try {
            // delay of 1 second for owner to put books
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/customer/addBookToCart?id=1&quantity=2&isbn=1",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        // Response Asserted
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        System.out.println("USER AFTER...");
        System.out.println(makeGetRequestWithUser());
        assertThat(makeGetRequestWithUser()).contains("To Kill a Mockingbird");
    }
}