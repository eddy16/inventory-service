package com.ecirilo.dao.repository;

import com.ecirilo.dao.document.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {

    Item getByCode(String code);

    void deleteByStockId(String stockId);
}
