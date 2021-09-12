package com.ecirilo.controller;

import com.ecirilo.controller.request.ItemQuantityRequest;
import com.ecirilo.controller.request.ItemRequest;

public final class InventoryMock {

    public static ItemRequest createItemRequest() {
        return ItemRequest.builder()
                .code("904123")
                .name("Monitor")
                .description("Gaming display")
                .build();
    }

    public static ItemQuantityRequest createItemQuantityRequest() {
        return ItemQuantityRequest.builder()
                .code("324567")
                .quantity(4)
                .name("TVs")
                .build();
    }
}
