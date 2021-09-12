package com.ecirilo.service.impl;

import com.ecirilo.dao.document.Inventory;
import com.ecirilo.dao.document.Item;
import com.ecirilo.dao.repository.InventoryRepository;
import com.ecirilo.dao.repository.ItemRepository;
import com.ecirilo.service.IInventoryService;
import com.ecirilo.service.dto.ItemDTO;
import com.ecirilo.service.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class InventoryServiceImpl implements IInventoryService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private final Random random;

    public InventoryServiceImpl(ItemMapper itemMapper,
                                ItemRepository itemRepository,
                                InventoryRepository inventoryRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        random = new Random();
    }


    @Override
    public void createItem(ItemDTO itemDTO) {
        if (itemDTO == null) {
            throw new IllegalArgumentException("No null item is allowed");
        }
        String stockId = createStockId(itemDTO.getName());
        Item item = itemMapper.itemDTOToItem(itemDTO, stockId);
        itemRepository.save(item);
    }

    @Override
    public ItemDTO getItem(String code) {
        Item item = itemRepository.getByCode(code);
        return itemMapper.itemToItemDTO(item);
    }

    @Override
    public void deleteItem(String code) {
        Item item = itemRepository.getByCode(code);
        if (item != null){
            itemRepository.deleteByStockId(item.getStockId());
        }
    }

    @Override
    public void addItemQuantity(String code, String name, int quantity) {
        Item item = itemRepository.getByCode(code);
        if (item != null){
            Inventory inventory = inventoryRepository.getByStockId(item.getStockId());
            if (inventory == null) {
                Inventory newInventory = Inventory.builder()
                        .stockId(item.getStockId())
                        .name(name)
                        .quantity(quantity)
                        .build();
                inventoryRepository.save(newInventory);
            } else {
                inventory.setQuantity(quantity);
                inventoryRepository.save(inventory);
            }
        } else {
            throw new IllegalStateException("No item for the given code was found");
        }
    }

    private String createStockId(String name){
        StringBuilder builder = new StringBuilder();
        String firstTwo = name.substring(0,2);
        String lastOne = name.substring(name.length()-1);
        int number = random.nextInt(99);
        return  builder.append(firstTwo).append(number).append(lastOne).toString();
    }
}
