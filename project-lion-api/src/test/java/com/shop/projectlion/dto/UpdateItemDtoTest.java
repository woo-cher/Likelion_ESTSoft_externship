package com.shop.projectlion.dto;

import com.shop.projectlion.api.adminitem.dto.UpdateItemDto;
import com.shop.projectlion.domain.item.type.ItemSellStatus;

public class UpdateItemDtoTest {

    public static final UpdateItemDto TEST_DTO = UpdateItemDto.builder()
            .itemId(1L)
            .itemName("name")
            .price(3000)
            .itemDetail("detail")
            .stockNumber(555)
            .itemSellStatus(ItemSellStatus.SELL)
            .deliveryId(1L)
            .build();

}
