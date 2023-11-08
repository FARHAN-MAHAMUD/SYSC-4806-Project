package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByISBN(long ISBN);
}
