package com.shop.projectlion.web.orderhist.dto;

import com.shop.projectlion.domain.order.type.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    private Long orderId;
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private int totalPrice;
    private int totalDeliveryFee;

    private List<OrderItemHistDto> orderItemHistDtos;

    @Builder
    public OrderHistDto(Long orderId, LocalDateTime orderTime, OrderStatus orderStatus, long totalPrice, long totalDeliveryFee) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.orderStatus = orderStatus;
        this.totalPrice = (int) (totalPrice + totalDeliveryFee);
        this.totalDeliveryFee = (int) totalDeliveryFee;
    }
}
