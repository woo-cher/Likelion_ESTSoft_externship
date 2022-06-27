package com.shop.projectlion.api.adminitem.dto;

import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.type.ItemSellStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@ApiModel(description = "상품 수정 요청 시, body 정보")
public class UpdateItemDto {

    @ApiModelProperty(value = "수정할 상품 id", example = "1")
    private Long itemId;

    @ApiModelProperty(value = "상품 명", example = "테스트 상품명")
    private String itemName;

    @ApiModelProperty(value = "상품 가격", example = "30000")
    private int price;

    @ApiModelProperty(value = "상품 상세", example = "테스트 상품 상세")
    private String itemDetail;

    @ApiModelProperty(value = "상품 재고", example = "999")
    private int stockNumber;

    @ApiModelProperty(value = "상품 판매 상태", example = "SELL")
    private ItemSellStatus itemSellStatus;

    @ApiModelProperty(value = "배송 정보 id", example = "3")
    private Long deliveryId;

    @Builder
    public UpdateItemDto(Long itemId, String itemName, int price, String itemDetail, int stockNumber,
                         ItemSellStatus itemSellStatus, Long deliveryId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
        this.deliveryId = deliveryId;
    }

    public Item toEntity(Delivery delivery) {
        return Item.builder()
                .id(itemId)
                .itemName(itemName)
                .price(price)
                .itemDetail(itemDetail)
                .stockNumber(stockNumber)
                .itemSellStatus(itemSellStatus)
                .delivery(delivery)
                .build();
    }

}
