package com.shop.projectlion.api.adminitem.dto;

import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.entity.ItemImage;
import com.shop.projectlion.domain.item.type.ItemSellStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@Builder
public class ReadItemDto {

    private Long itemId;
    private String itemName;
    private int price;
    private String itemDetail;
    private int stockNumber;
    private ItemSellStatus itemSellStatus;
    private Long deliveryId;
    private int deliveryFee;
    private List<ItemImageDto> itemImageDtos;

    @Getter @Setter
    @Builder
    public static class ItemImageDto {
        private Long itemImageId;
        private String imageUrl;

        public static ItemImageDto of(ItemImage itemImage) {
            String imageUrl = itemImage.getImageUrl();

            return ItemImageDto.builder()
                    .itemImageId(itemImage.getId())
                    .imageUrl(imageUrl == null ? "" : imageUrl)
                    .build();
        }
    }

    public static ReadItemDto of(Item item) {
        List<ItemImageDto> itemImageDtos = item.getItemImages().stream()
                .map(ItemImageDto::of)
                .collect(Collectors.toList());

        return ReadItemDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .itemDetail(item.getItemDetail())
                .stockNumber(item.getStockNumber())
                .itemSellStatus(item.getItemSellStatus())
                .deliveryId(item.getDelivery().getId())
                .deliveryFee(item.getDelivery().getDeliveryFee())
                .itemImageDtos(itemImageDtos)
                .build();
    }

}
