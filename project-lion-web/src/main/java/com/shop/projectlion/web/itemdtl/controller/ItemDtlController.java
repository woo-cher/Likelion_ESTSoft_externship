package com.shop.projectlion.web.itemdtl.controller;

import com.shop.projectlion.global.config.security.UserDetailsImpl;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.web.itemdtl.dto.ItemDtlDto;
import com.shop.projectlion.web.itemdtl.service.ItemDtlService;
import com.shop.projectlion.web.order.dto.InsertOrderItemDto;
import com.shop.projectlion.web.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/itemdtl")
@Slf4j
public class ItemDtlController {

    private final ItemDtlService itemDtlService;
    private final OrderService orderService;

    @GetMapping(value = "/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){

        try {
            ItemDtlDto itemDtlDto = itemDtlService.findItemDtlDtoById(itemId);
            model.addAttribute("item", itemDtlDto);

            return "itemdtl/itemdtl";
        } catch (BusinessException e) {
            log.error(e.getMessage());

            model.addAttribute("item", ItemDtlDto.builder().itemId(itemId).build());
            model.addAttribute("dtlError", e.getErrorCode().getMessage());

            return "itemdtl/itemdtl";
        }
    }

    @ResponseBody
    @PostMapping("/order")
    public ResponseEntity<Object> saveOrder(@RequestBody InsertOrderItemDto insertOrderDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            orderService.insertOrder(insertOrderDto, userDetails);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
        }
    }
}
