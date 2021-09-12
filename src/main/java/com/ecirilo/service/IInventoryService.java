package com.ecirilo.service;

import com.ecirilo.service.dto.ItemDTO;

public interface IInventoryService {

    void createItem(ItemDTO itemDTO);

    ItemDTO getItem(String code);

    void deleteItem(String code);

    void addItemQuantity(String code, String name, int quantity);
}
