package com.ecirilo.controller;

import com.ecirilo.controller.exception.CustomExceptionHandler;
import com.ecirilo.controller.mapper.InventoryAPIMapperImpl;
import com.ecirilo.controller.response.ItemResponse;
import com.ecirilo.controller.request.ItemQuantityRequest;
import com.ecirilo.controller.request.ItemRequest;
import com.ecirilo.service.IInventoryService;
import com.ecirilo.service.dto.ItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InventoryController.class, InventoryAPIMapperImpl.class, CustomExceptionHandler.class})
class InventoryControllerTest {

    @MockBean
    private IInventoryService inventoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EasyRandom easyRandom = new EasyRandom();

    @Autowired
    MockMvc mockMvc;

    @Test
    void createItem() throws Exception {
        doNothing().when(inventoryService).createItem(any());
        ItemRequest itemRequest = InventoryMock.createItemRequest();
        mockMvc.perform(post("/inventory/item")
                .content(objectMapper.writeValueAsString(itemRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createItem_BadRequest() throws Exception {
        doNothing().when(inventoryService).createItem(any());
        ItemRequest itemRequest = InventoryMock.createItemRequestNotCompliant();
        mockMvc.perform(post("/inventory/item")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertThat(mvcResult.getResolvedException()).isInstanceOf(MethodArgumentNotValidException.class))
                .andExpect(jsonPath("$.errors")
                        .value(hasItems("code:must not be blank",
                        "name:must not be blank",
                        "description:size must be between 0 and 35")));
    }

    @Test
    void getItem() throws Exception {
        ItemDTO itemDTO = easyRandom.nextObject(ItemDTO.class);
        when(inventoryService.getItem(anyString())).thenReturn(itemDTO);
        MvcResult mvcResult = mockMvc.perform(get("/inventory/item/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        ItemResponse itemResponse = objectMapper.readValue(responseBody, ItemResponse.class);
        assertThat(itemResponse).usingRecursiveComparison().isEqualTo(itemDTO);
    }

    @Test
    void deleteItem() throws Exception{
        doNothing().when(inventoryService).deleteItem(anyString());
        mockMvc.perform(delete("/inventory/item/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void addItemQuantity() throws Exception {
        ItemQuantityRequest itemQuantityRequest = InventoryMock.createItemQuantityRequest();
        doNothing().when(inventoryService).addItemQuantity(anyString(), anyString(), anyInt());
        mockMvc.perform(post("/inventory/item/quantity")
                .content(objectMapper.writeValueAsString(itemQuantityRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
