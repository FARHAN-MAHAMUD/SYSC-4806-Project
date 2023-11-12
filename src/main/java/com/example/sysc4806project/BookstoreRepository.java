package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;

public interface BookstoreRepository extends CrudRepository<Book, Long> {

    Book findByISBN(long ISBN);
    boolean existsByISBN(long ISBN);
}
