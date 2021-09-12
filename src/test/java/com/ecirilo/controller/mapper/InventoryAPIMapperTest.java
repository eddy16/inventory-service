package com.ecirilo.controller.mapper;

import com.ecirilo.controller.response.ItemResponse;
import com.ecirilo.controller.request.ItemRequest;
import com.ecirilo.service.dto.ItemDTO;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryAPIMapperTest {

    private EasyRandom easyRandom;
    private InventoryAPIMapper mapper;

    @BeforeEach
    void setup() {
        easyRandom = new EasyRandom();
        mapper = Mappers.getMapper(InventoryAPIMapper.class);
    }

    @Test
    @DisplayName("Should map from ItemRequest to ItemDTO")
    void itemRequestToItemDTO() {
        ItemRequest itemRequest = easyRandom.nextObject(ItemRequest.class);
        ItemDTO itemDTO = mapper.itemRequestToItemDTO(itemRequest);
        assertThat(itemDTO)
                .usingRecursiveComparison()
                .isEqualTo(itemRequest);
    }

    @Test
    @DisplayName("Should map from ItemDTO to ItemResponse")
    void itemDTOToItemResponse() {
        ItemDTO itemDTO = easyRandom.nextObject(ItemDTO.class);
        ItemResponse itemResponse = mapper.itemDTOToItemResponse(itemDTO);
        assertThat(itemDTO)
                .usingRecursiveComparison()
                .isEqualTo(itemResponse);
    }
}
