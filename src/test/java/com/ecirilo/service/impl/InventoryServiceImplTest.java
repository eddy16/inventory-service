package com.ecirilo.service.impl;

import com.ecirilo.dao.document.Inventory;
import com.ecirilo.dao.document.Item;
import com.ecirilo.dao.repository.InventoryRepository;
import com.ecirilo.dao.repository.ItemRepository;
import com.ecirilo.service.IInventoryService;
import com.ecirilo.service.dto.ItemDTO;
import com.ecirilo.service.mapper.ItemMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Captor
    ArgumentCaptor<Item> itemCaptor;

    @Captor
    ArgumentCaptor<Inventory> inventoryCaptor;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    private EasyRandom easyRandom;
    private IInventoryService inventoryService;

    @BeforeEach
    void setup() {
        easyRandom = new EasyRandom();
        ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
        inventoryService = new InventoryServiceImpl(itemMapper, itemRepository, inventoryRepository);
    }

    @Test
    @DisplayName("Should save an item with generated stockId")
    void createItem_HappyPath_Success() {
        ItemDTO itemDTO = new ItemDTO("907856", "Notebook", "Portable computer");
        inventoryService.createItem(itemDTO);
        verify(itemRepository).save(itemCaptor.capture());
        Item item = itemCaptor.getValue();
        assertThat(item.getStockId())
                .isNotBlank()
                .startsWith("No")
                .endsWith("k");
    }

    @Test
    @DisplayName("Should throw an IllegalArgumentException")
    void createItem_NullParam_Fail() {
        Throwable throwable = catchThrowable(() -> inventoryService.createItem(null));

        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No null item is allowed");
    }

    @Test
    @DisplayName("Should get item by given code")
    void getItem() {
        Item item = easyRandom.nextObject(Item.class);
        when(itemRepository.getByCode(anyString())).thenReturn(item);
        ItemDTO itemDTO = inventoryService.getItem("902156");
        assertThat(itemDTO)
                .usingRecursiveComparison()
                .isEqualTo(item);
        verify(itemRepository).getByCode(anyString());
    }

    @Test
    @DisplayName("Should delete item by given code")
    void deleteItem_ItemExists_Success() {
        Item item = easyRandom.nextObject(Item.class);
        when(itemRepository.getByCode("785612")).thenReturn(item);
        inventoryService.deleteItem("785612");
        verify(itemRepository).getByCode("785612");
        verify(itemRepository).deleteByStockId(item.getStockId());
    }

    @Test
    @DisplayName("Should not delete anything")
    void deleteItem_ItemNotExists_Success() {
        when(itemRepository.getByCode("563476")).thenReturn(null);
        inventoryService.deleteItem("563476");
        verify(itemRepository).getByCode("563476");
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    @DisplayName("Should save new inventory with existing item")
    void addItemQuantity_SaveNewInventory_Success() {
        Item item = easyRandom.nextObject(Item.class);
        when(itemRepository.getByCode("785612")).thenReturn(item);
        when(inventoryRepository.getByStockId(item.getStockId())).thenReturn(null);
        inventoryService.addItemQuantity("785612", "TV", 4);
        verify(inventoryRepository).save(inventoryCaptor.capture());
        Inventory capturedInventory = inventoryCaptor.getValue();
        assertThat(capturedInventory.getStockId()).isEqualTo(item.getStockId());
        assertThat(capturedInventory.getName()).isEqualTo("TV");
        assertThat(capturedInventory.getQuantity()).isEqualTo(4);
        verify(inventoryRepository).getByStockId(anyString());
        verify(itemRepository).getByCode(anyString());
    }

    @Test
    @DisplayName("Should update existing inventory with existing item")
    void addItemQuantity_UpdateInventory_Success() {
        Item item = easyRandom.nextObject(Item.class);
        Inventory inventory = easyRandom.nextObject(Inventory.class);
        when(itemRepository.getByCode("785612")).thenReturn(item);
        when(inventoryRepository.getByStockId(item.getStockId())).thenReturn(inventory);
        inventoryService.addItemQuantity("785612", "TV", 4);
        verify(inventoryRepository).save(inventoryCaptor.capture());
        Inventory capturedInventory = inventoryCaptor.getValue();
        assertThat(capturedInventory).usingRecursiveComparison().ignoringFields("quantity").isEqualTo(inventory);
        assertThat(capturedInventory.getName()).isNotEqualTo("TV");
        assertThat(capturedInventory.getQuantity()).isEqualTo(4);
        verify(inventoryRepository).getByStockId(anyString());
        verify(itemRepository).getByCode(anyString());
    }

    @Test
    @DisplayName("Should thrown a IllegalStateException when the item not exits")
    void addItemQuantity_ItemNotExists_Fail() {
        when(itemRepository.getByCode(anyString())).thenReturn(null);
        Throwable throwable = catchThrowable(() ->
                inventoryService.addItemQuantity("344565", "Micro", 2));
        assertThat(throwable)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No item for the given code was found");
    }
}
