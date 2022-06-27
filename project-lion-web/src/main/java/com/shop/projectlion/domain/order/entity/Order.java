package com.shop.projectlion.domain.order.entity;

import com.shop.projectlion.domain.base.BaseEntity;
import com.shop.projectlion.domain.member.entity.Member;
import com.shop.projectlion.domain.order.type.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {

    @Id
    @Column(length = 20, name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(length = 6, updatable = false)
    @CreatedDate
    private LocalDateTime orderTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Builder
    public Order(Long id, OrderStatus orderStatus, LocalDateTime orderTime, Member member, List<OrderItem> orderItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.member = member;
        this.orderItems = orderItems;
    }

    public static Order createOrder(Member member) {
        return Order.builder()
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .build();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateOrderItems(List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> orderItem.updateOrder(this));
        this.orderItems = orderItems;
    }
}
