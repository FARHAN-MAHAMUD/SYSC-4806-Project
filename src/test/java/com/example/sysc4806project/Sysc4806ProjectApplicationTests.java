package com.example.sysc4806project;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This integration test starts the server with a random port number & waits for a connection.
 * It then sends HTTP requests & asserts the responses for the templates.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Sysc4806ProjectApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // Spring Boot automatically provides this

    @Autowired
    private BookstoreController bookstoreController;

    /**
     * This test makes sure the context is creating the BookstoreController by injecting it before running the server.
     * @throws Exception
     */
    @Test
    void contextLoads() throws Exception {
        assertThat(bookstoreController).isNotNull();
    }

    /**
     * This test makes sure the index home template returns the right information by comparing the string contents.
     * @throws Exception
     */
    @Test
    void homeShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("<title>Main Page</title>");
    }

    /**
     * This test makes sure the customer template returns the right information by comparing the string contents.
     * @throws Exception
     */
    @Test
    @WithMockUser
    void customerShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customer",
                String.class)).contains("<title>Customer</title>");
    }

    /**
     * This test makes sure the owner template returns the right information by comparing the string contents.
     * @throws Exception
     */
    @WithMockUser
    @Test
    void ownerShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/owner",
                String.class)).contains("<title>Owner</title>");
    }
}
