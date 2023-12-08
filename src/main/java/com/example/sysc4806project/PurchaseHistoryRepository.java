package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * An interface for creating a database for storing the purchases
 */
public interface PurchaseHistoryRepository extends CrudRepository<PurchaseHistory, Long>  {

    /**
     * Gets a list of purchases based on the given user
     * @param user
     * @return
     */
    List<PurchaseHistory> findByUser(User user);

    /**
     * Gets a list of purchases based on the given book
     * @param user
     * @return
     */
    List<PurchaseHistory> findByBook(Book book);
}
