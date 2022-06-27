package com.shop.projectlion.web.adminitem.dto;

import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.entity.ItemImage;
import com.shop.projectlion.domain.item.type.ItemSellStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class UpdateItemDto {

    private Long itemId;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    @NotNull(message = "배송 정보는 필수 입력 값입니다.")
    private Long deliveryId;

    private List<MultipartFile> itemImageFiles;

    private List<ItemImageDto> itemImageDtos;

    private List<String> originalImageNames;

    @Builder
    @Getter
    @Setter
    public static class ItemImageDto {
        private Long itemImageId;
        private String originalImageName;
    }

    public static UpdateItemDto of(Item item) {
        List<ItemImageDto> itemImageDtos = new ArrayList<>();

        item.getItemImages().forEach(itemImage -> itemImageDtos.add(ItemImageDto.builder()
                .itemImageId(itemImage.getId())
                .originalImageName(itemImage.getOriginalImageName())
                .build()
        ));

        return UpdateItemDto.builder()
                .itemId(item.getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemSellStatus(item.getItemSellStatus())
                .stockNumber(item.getStockNumber())
                .price(item.getPrice())
                .deliveryId(item.getDelivery().getId())
                .itemImageDtos(itemImageDtos)
                .build();
    }

    public void setItemImageDtos() {
        this.itemImageDtos = new ArrayList<>();
        this.originalImageNames.forEach(name ->
                itemImageDtos.add(ItemImageDto.builder().originalImageName(name).build())
        );
    }

    public Item toEntity(Delivery delivery, List<ItemImage> dbImages) {
        return Item.builder()
                .id(itemId)
                .itemName(itemName)
                .price(price)
                .itemDetail(itemDetail)
                .stockNumber(stockNumber)
                .itemSellStatus(itemSellStatus)
                .delivery(delivery)
                .member(delivery.getMember())
                .itemImages(dbImages)
                .build();
    }
}
