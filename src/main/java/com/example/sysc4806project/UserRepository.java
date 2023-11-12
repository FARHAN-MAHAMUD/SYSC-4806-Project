package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Book, Long> {

    User findById(long id);
}
