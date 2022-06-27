package com.shop.projectlion.web.itemdtl.dto;

import com.shop.projectlion.domain.item.type.ItemSellStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDtlDto {

    private Long itemId;

    private String itemName;

    private Integer price;

    private String itemDetail;

    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private Integer deliveryFee;

    private List<ItemImageDto> itemImageDtos = new ArrayList<>();

    @Getter @Setter
    public static class ItemImageDto {
        private String imageUrl;

        @Builder
        public ItemImageDto(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    @Builder
    public ItemDtlDto(Long itemId, String itemName, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus, Integer deliveryFee) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
        this.deliveryFee = deliveryFee;
    }
}
