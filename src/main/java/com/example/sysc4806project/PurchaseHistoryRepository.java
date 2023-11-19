package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface PurchaseHistoryRepository extends CrudRepository<PurchaseHistory, Long>  {

    List<PurchaseHistory> findByUser(User user);
}
