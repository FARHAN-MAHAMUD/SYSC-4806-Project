package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This is a crud repository that contains all Users in the system and stores their information.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Used to search a user by unique id.
     * 
     * @param id long, unique identifier for the user.
     * @return User, an object representing a user of the store.
     */
    User findById(long id);

    /**
     * Returns a list of all Users existing in the store repository.
     */
    List<User> findAll();

    /**
     * Used to find a user by their username
     * @param name String, the name of the user
     * @return User, an object representing a user of the store.
     */
    User findByName(String name);
}
