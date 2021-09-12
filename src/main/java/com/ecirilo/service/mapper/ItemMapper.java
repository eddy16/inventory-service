package com.ecirilo.service.mapper;

import com.ecirilo.dao.document.Item;
import com.ecirilo.service.dto.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {

    @Mapping(source = "stockId", target = "stockId")
    @Mapping(source = "itemDTO.name", target = "name")
    @Mapping(source = "itemDTO.code", target = "code")
    @Mapping(source = "itemDTO.description", target = "description")
    Item itemDTOToItem(ItemDTO itemDTO, String stockId);

    ItemDTO itemToItemDTO(Item item);
}
