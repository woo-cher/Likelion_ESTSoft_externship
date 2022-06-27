package com.shop.projectlion.web.main.controller;

import com.shop.projectlion.global.constant.NumberConstant;
import com.shop.projectlion.web.main.dto.ItemSearchDto;
import com.shop.projectlion.web.main.dto.MainItemDto;
import com.shop.projectlion.web.main.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService mainService;

    @GetMapping("/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

        int pageCount = page.orElse(NumberConstant.PAGE_DEFAULT_COUNT.value());
        int size = NumberConstant.PAGE_ITEM_MAX_COUNT.value();

        Pageable pageable = PageRequest.of(pageCount, size);
        Page<MainItemDto> pageMainItemDto = mainService.findAllMainItemDtos(itemSearchDto.getSearchQuery(), pageable);

        model.addAttribute("items", pageMainItemDto);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main/mainpage";
    }

}
