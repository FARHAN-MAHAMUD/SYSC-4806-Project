package com.example.sysc4806project;

import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    Inventory findByBook(Book book);
}
