package com.shop.projectlion.web.itemdtl.service;

import com.shop.projectlion.domain.item.repostiory.ItemRepository;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.web.adminitem.service.AdminItemService;
import com.shop.projectlion.web.itemdtl.dto.ItemDtlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemDtlService {

    private final ItemRepository itemRepository;
    private final AdminItemService adminItemService;

    @Transactional
    public ItemDtlDto findItemDtlDtoById(Long itemId) {
        ItemDtlDto itemDtlDto = itemRepository.findItemDtlDtoById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        Long id = itemDtlDto.getItemId();
        itemDtlDto.setItemImageDtos(adminItemService.findAllItemById(id, ItemDtlDto.ItemImageDto.class));

        return itemDtlDto;
    }
}
