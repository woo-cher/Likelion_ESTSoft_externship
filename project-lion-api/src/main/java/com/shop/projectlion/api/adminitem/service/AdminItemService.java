package com.shop.projectlion.api.adminitem.service;

import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.delivery.service.DeliveryService;
import com.shop.projectlion.api.adminitem.dto.ReadItemDto;
import com.shop.projectlion.api.adminitem.dto.UpdateItemDto;
import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.repostiory.ItemRepository;
import com.shop.projectlion.global.error.exception.EntityNotFoundException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminItemService {

    private final ItemRepository itemRepository;
    private final DeliveryService deliveryService;

    public ReadItemDto findItemById(Long itemId) {
        Item dbItem = itemRepository.findByIdWithJoinFetch(itemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ITEM_NOT_EXISTS));

        return ReadItemDto.of(dbItem);
    }

    @Transactional
    public UpdateItemDto updateItemById(Long itemId, UpdateItemDto updateItemDto) {
        Item dbItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ITEM_NOT_EXISTS));

        Delivery delivery = deliveryService.findDeliveryById(updateItemDto.getDeliveryId());

        Item updateItem = updateItemDto.toEntity(delivery);
        dbItem.updateByItem(updateItem);

        return updateItemDto;
    }

}
