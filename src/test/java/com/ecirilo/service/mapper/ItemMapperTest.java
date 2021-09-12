package com.ecirilo.service.mapper;

import com.ecirilo.dao.document.Item;
import com.ecirilo.service.dto.ItemDTO;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private EasyRandom easyRandom;
    private ItemMapper mapper;

    @BeforeEach
    void setup() {
        easyRandom = new EasyRandom();
        mapper = Mappers.getMapper(ItemMapper.class);
    }

    @Test
    @DisplayName("Should map from ItemDTO to Item")
    void itemDTOToItem() {
        String stockId = "AM809";
        ItemDTO itemDTO = easyRandom.nextObject(ItemDTO.class);
        Item item = mapper.itemDTOToItem(itemDTO, stockId);
        assertThat(item)
                .usingRecursiveComparison()
                .ignoringFields("id", "stockId")
                .isEqualTo(itemDTO);
        assertThat(item.getStockId()).isEqualTo(stockId);
    }

    @Test
    @DisplayName("Should map from Item to ItemDTO")
    void itemToItemDTO() {
        Item item = easyRandom.nextObject(Item.class);
        ItemDTO itemDTO = mapper.itemToItemDTO(item);
        assertThat(itemDTO)
                .usingRecursiveComparison()
                .isEqualTo(item);
    }
}
