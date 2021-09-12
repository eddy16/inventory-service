package com.ecirilo.controller.mapper;

import com.ecirilo.controller.response.ItemResponse;
import com.ecirilo.controller.request.ItemRequest;
import com.ecirilo.service.dto.ItemDTO;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryAPIMapper {

    ItemDTO itemRequestToItemDTO(ItemRequest itemRequest);
    ItemResponse itemDTOToItemResponse(ItemDTO itemDTO);
}
