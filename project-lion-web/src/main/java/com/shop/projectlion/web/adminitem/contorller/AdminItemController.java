package com.shop.projectlion.web.adminitem.contorller;

import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.validator.InsertItemValidator;
import com.shop.projectlion.domain.item.validator.UpdateItemValidator;
import com.shop.projectlion.global.config.security.UserDetailsImpl;
import com.shop.projectlion.web.adminitem.dto.DeliveryDto;
import com.shop.projectlion.web.adminitem.dto.InsertItemDto;
import com.shop.projectlion.web.adminitem.dto.UpdateItemDto;
import com.shop.projectlion.web.adminitem.service.AdminItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/items")
@Slf4j
public class AdminItemController {

    private final AdminItemService adminItemService;
    private final InsertItemValidator itemImageValidator;
    private final UpdateItemValidator updateItemValidator;

    @GetMapping("/new")
    public String itemForm(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<DeliveryDto> deliveryDtos = adminItemService.findAllDeliveryByMember(userDetails.getMember().getId());
        log.info("db deliveries : {}", deliveryDtos);

        model.addAttribute("deliveryDtos", deliveryDtos);
        model.addAttribute("insertItemDto", new InsertItemDto());

        return "adminitem/registeritemform";
    }

    @PostMapping("/new")
    public String itemSave(@ModelAttribute @Validated InsertItemDto insertItemDto, BindingResult bindingResult,
                       @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) throws IOException {

        itemImageValidator.validate(insertItemDto, bindingResult);

        if (bindingResult.hasErrors()) {
            List<DeliveryDto> deliveryDtos = adminItemService.findAllDeliveryByMember(userDetails.getMember().getId());
            model.addAttribute("deliveryDtos", deliveryDtos);
            return "adminitem/registeritemform";
        }

        Item insertedItem = adminItemService.insertItem(insertItemDto, userDetails.getMember());

        return "redirect:/admin/items/" + insertedItem.getId();
    }

    @GetMapping("/{itemId}")
    public String itemEdit(@PathVariable Long itemId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<DeliveryDto> deliveryDtos = adminItemService.findAllDeliveryByMember(userDetails.getMember().getId());
        UpdateItemDto updateItemDto = adminItemService.findItem(itemId);

        model.addAttribute("deliveryDtos", deliveryDtos);
        model.addAttribute("updateItemDto", updateItemDto);

        return "adminitem/updateitemform";
    }

    @PostMapping("/{itemId}")
    public String itemEdit(@PathVariable Long itemId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                           @ModelAttribute @Validated UpdateItemDto updateItemDto, BindingResult bindingResult,
                           Model model) throws IOException {

        updateItemValidator.validate(updateItemDto, bindingResult);

        if (bindingResult.hasErrors()) {
            List<DeliveryDto> deliveryDtos = adminItemService.findAllDeliveryByMember(userDetails.getMember().getId());
            updateItemDto.setItemImageDtos();

            model.addAttribute("deliveryDtos", deliveryDtos);
            model.addAttribute("updateItemDto", updateItemDto);
            return "adminitem/updateitemform";
        }

        adminItemService.updateItem(itemId, updateItemDto);
        return "redirect:/admin/items/" + itemId;
    }
}
