package com.shop.projectlion.web.orderhist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemHistDto {

    private String itemName;
    private int count;
    private int orderPrice;
    private String imageUrl;

    @Builder
    public OrderItemHistDto(String itemName, int count, int orderPrice, String imageUrl) {
        this.itemName = itemName;
        this.count = count;
        this.orderPrice = orderPrice;
        this.imageUrl = imageUrl;
    }
}
