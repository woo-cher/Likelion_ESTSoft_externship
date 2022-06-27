package com.shop.projectlion.web.order.service;

import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.order.entity.Order;
import com.shop.projectlion.domain.order.entity.OrderItem;
import com.shop.projectlion.domain.order.repository.OrderRepository;
import com.shop.projectlion.domain.order.type.OrderStatus;
import com.shop.projectlion.global.config.security.UserDetailsImpl;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.web.adminitem.service.AdminItemService;
import com.shop.projectlion.web.order.dto.InsertOrderItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final AdminItemService adminItemService;
    private final OrderRepository orderRepository;

    @Transactional
    public void insertOrder(InsertOrderItemDto insertOrderDto, UserDetailsImpl userDetails) {

        Item item = adminItemService.findItemById(insertOrderDto.getItemId());

        // check stock number
        if (item.getStockNumber() < insertOrderDto.getCount()) {
            throw new BusinessException(ErrorCode.NOT_ENOUGH_STOCK_NUMBER, stockNumberErrorMessage(item.getStockNumber()));
        }

        // update stock number
        item.updateStockNumber(item.getStockNumber() - insertOrderDto.getCount());

        // save order and order_items
        Order order = Order.createOrder(userDetails.getMember());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(insertOrderDto.toEntity(order, item));

        order.updateOrderItems(orderItems);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderItem> orderItems = order.getOrderItems();

        orderItems.forEach(orderItem -> {
            Item item = orderItem.getItem();
            int newStockNumber = item.getStockNumber() + orderItem.getCount();
            item.updateStockNumber(newStockNumber);
        });

        order.updateOrderStatus(OrderStatus.CANCEL);
    }

    private String stockNumberErrorMessage(int remain) {
        return String.format(" (현재 재고 수량 : %d)", remain);
    }

}
