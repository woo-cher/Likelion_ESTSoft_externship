package com.shop.projectlion.web.orderhist.controller;

import com.shop.projectlion.global.config.security.UserDetailsImpl;
import com.shop.projectlion.global.constant.NumberConstant;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.web.order.service.OrderService;
import com.shop.projectlion.web.orderhist.dto.OrderHistDto;
import com.shop.projectlion.web.orderhist.service.OrderHistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orderhist")
@Slf4j
public class OrderHistController {

    private final OrderHistService orderHistService;
    private final OrderService orderService;

    @GetMapping
    public String orderHist(Optional<Integer> page, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        int pageCount = page.orElse(NumberConstant.PAGE_DEFAULT_COUNT.value());
        int size = NumberConstant.PAGE_ITEM_MAX_COUNT.value();

        Pageable pageable = PageRequest.of(pageCount, size, Sort.by("orderStatus").descending()
                .and(Sort.by("orderTime").descending()));
        Page<OrderHistDto> pageOrderHistDtos = orderHistService.findAllOrderHistDto(userDetails, pageable);

        model.addAttribute("orders", pageOrderHistDtos);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", NumberConstant.PAGE_BTN_MAX_COUNT.value());

        return "orderhist/orderhist";
    }

    @ResponseBody
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable Long orderId) {

        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
        }
    }

}

