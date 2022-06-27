package com.shop.projectlion.domain.order.entity;

import com.shop.projectlion.domain.base.BaseEntity;
import com.shop.projectlion.domain.item.entity.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {

    @Id
    @Column(length = 20, name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11)
    private int count;

    @Column(length = 11)
    private int orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Builder
    public OrderItem(Long id, int count, int orderPrice, Item item, Order order) {
        this.id = id;
        this.count = count;
        this.orderPrice = orderPrice;
        this.item = item;
        this.order = order;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }
}
