package com.ecirilo.dao.repository;

import com.ecirilo.dao.document.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<Inventory, String> {

    Inventory getByStockId(String stockId);
}
