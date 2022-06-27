package com.shop.projectlion.web.adminitem.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDto {

    private Long id;
    private String deliveryName;
    private int deliveryFee;

    @Builder
    public DeliveryDto(Long id, String deliveryName, int deliveryFee) {
        this.id = id;
        this.deliveryName = deliveryName;
        this.deliveryFee = deliveryFee;
    }
}
