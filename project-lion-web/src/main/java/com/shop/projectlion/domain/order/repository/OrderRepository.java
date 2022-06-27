package com.shop.projectlion.domain.order.repository;

import com.shop.projectlion.domain.order.entity.Order;
import com.shop.projectlion.web.orderhist.dto.OrderHistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.shop.projectlion.web.orderhist.dto.OrderHistDto(o.id, o.orderTime, o.orderStatus, " +
            "SUM(oi.orderPrice * oi.count), SUM(d.deliveryFee)) FROM orders o " +
            "JOIN order_item oi ON oi.order.id = o.id " +
            "JOIN item i ON oi.item.id = i.id " +
            "JOIN delivery d ON i.delivery.id = d.id " +
            "WHERE o.member.id = :memberId GROUP BY o.id")
    Page<OrderHistDto> findAllOrderHistDtosByMember(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT o FROM orders o JOIN FETCH o.orderItems oi JOIN FETCH oi.item i WHERE o.id = :orderId")
    Optional<Order> findByOrderId(@Param("orderId") Long orderId);
}
