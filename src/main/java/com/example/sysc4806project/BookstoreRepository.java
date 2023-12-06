package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;

/**
 * An interface for a database for the books stored in the book store
 */
public interface BookstoreRepository extends CrudRepository<Book, Long> {

    /**
     * Finds a book by their unique ISBN
     * @param ISBN The ISBN of the book a user wants to find
     * @return
     */
    Book findByISBN(long ISBN);

    /**
     * Returns true if there is book with the given ISBN exists in the databse
     * @param ISBN
     * @return
     */
    boolean existsByISBN(long ISBN);
}
