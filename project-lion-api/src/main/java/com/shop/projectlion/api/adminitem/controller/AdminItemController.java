package com.shop.projectlion.api.adminitem.controller;

import com.shop.projectlion.api.adminitem.service.AdminItemService;
import com.shop.projectlion.api.adminitem.dto.ReadItemDto;
import com.shop.projectlion.api.adminitem.dto.UpdateItemDto;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/items")
public class AdminItemController {

    private final AdminItemService adminItemService;

    @GetMapping("/{itemId}")
    public ReadItemDto read(@PathVariable Long itemId) {
        return adminItemService.findItemById(itemId);
    }

    @ApiOperation(value = "상품 정보 업데이트 api")
    @PatchMapping("/{itemId}")
    public UpdateItemDto update(@ApiParam(value = "수정 할 상품 id", required = true) @PathVariable Long itemId,
                                @RequestBody UpdateItemDto updateItemDto) {

        if (!itemId.equals(updateItemDto.getItemId())) {
            throw new BusinessException(ErrorCode.NOT_EQUALS_ENTITY_ID);
        }

        return adminItemService.updateItemById(itemId, updateItemDto);
    }
}
