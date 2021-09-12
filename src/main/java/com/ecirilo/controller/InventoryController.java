package com.ecirilo.controller;

import com.ecirilo.controller.mapper.InventoryAPIMapper;
import com.ecirilo.controller.response.ItemResponse;
import com.ecirilo.controller.request.ItemQuantityRequest;
import com.ecirilo.controller.request.ItemRequest;
import com.ecirilo.service.IInventoryService;
import com.ecirilo.service.dto.ItemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final IInventoryService inventoryService;
    private final InventoryAPIMapper mapper;

    public InventoryController(IInventoryService inventoryService,
                               InventoryAPIMapper mapper) {
        this.inventoryService = inventoryService;
        this.mapper = mapper;
    }

    @PostMapping("/item")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createItem(@Valid @RequestBody ItemRequest itemRequest){
        ItemDTO itemDTO = mapper.itemRequestToItemDTO(itemRequest);
        inventoryService.createItem(itemDTO);
    }

    @GetMapping("/item/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponse getItem(@PathVariable("code") String code){
        ItemDTO itemDTO = inventoryService.getItem(code);
        return mapper.itemDTOToItemResponse(itemDTO);
    }

    @DeleteMapping("/item/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable("code") String code){
        inventoryService.deleteItem(code);
    }

    @PostMapping("/item/quantity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItemQuantity(@Valid @RequestBody ItemQuantityRequest itemQuantityRequest){
        inventoryService.addItemQuantity(itemQuantityRequest.getCode(), itemQuantityRequest.getName(), itemQuantityRequest.getQuantity());
    }
}
