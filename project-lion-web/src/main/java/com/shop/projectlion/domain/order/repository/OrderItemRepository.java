package com.shop.projectlion.domain.order.repository;

import com.shop.projectlion.domain.order.entity.OrderItem;
import com.shop.projectlion.web.orderhist.dto.OrderItemHistDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT new com.shop.projectlion.web.orderhist.dto.OrderItemHistDto(i.itemName, oi.count, oi.orderPrice, img.imageUrl) " +
            "FROM order_item oi JOIN item i ON oi.item.id = i.id " +
            "JOIN item_image img ON i.id = img.item.id " +
            "WHERE oi.order.id = :orderId AND img.isRepImage = true")
    List<OrderItemHistDto> findAllByOrderId(@Param("orderId") Long orderId);
}
