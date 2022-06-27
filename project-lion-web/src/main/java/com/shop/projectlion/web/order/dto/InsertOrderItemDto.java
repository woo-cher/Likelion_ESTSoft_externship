package com.shop.projectlion.web.order.dto;

import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.order.entity.Order;
import com.shop.projectlion.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class InsertOrderItemDto {
    private Integer count;
    private Integer orderPrice;
    private Long itemId;

    public OrderItem toEntity(Order order, Item item) {
        return OrderItem.builder()
                .order(order)
                .count(count)
                .item(item)
                .orderPrice(orderPrice).build();
    }
}
