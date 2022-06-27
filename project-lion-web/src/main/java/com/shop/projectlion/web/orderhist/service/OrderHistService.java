package com.shop.projectlion.web.orderhist.service;

import com.shop.projectlion.domain.order.repository.OrderItemRepository;
import com.shop.projectlion.domain.order.repository.OrderRepository;
import com.shop.projectlion.global.config.security.UserDetailsImpl;
import com.shop.projectlion.web.orderhist.dto.OrderHistDto;
import com.shop.projectlion.web.orderhist.dto.OrderItemHistDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderHistService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Page<OrderHistDto> findAllOrderHistDto(UserDetailsImpl userDetails, Pageable pageable) {
        Page<OrderHistDto> orderHistDtoPage = orderRepository.findAllOrderHistDtosByMember(
                userDetails.getMember().getId(), pageable);

        orderHistDtoPage.forEach(orderHistDto -> {
            List<OrderItemHistDto> orderItemHistDtos = orderItemRepository.findAllByOrderId(orderHistDto.getOrderId());
            orderHistDto.setOrderItemHistDtos(orderItemHistDtos);
        });

        return orderHistDtoPage;
    }
}
