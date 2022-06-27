package com.shop.projectlion.web.adminitem.service;

import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.delivery.repository.DeliveryRepository;
import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.entity.ItemImage;
import com.shop.projectlion.domain.item.repostiory.ItemImageRepository;
import com.shop.projectlion.domain.item.repostiory.ItemRepository;
import com.shop.projectlion.domain.member.entity.Member;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.infra.FileService;
import com.shop.projectlion.infra.UploadFile;
import com.shop.projectlion.web.adminitem.dto.DeliveryDto;
import com.shop.projectlion.web.adminitem.dto.InsertItemDto;
import com.shop.projectlion.web.adminitem.dto.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminItemService {

    private final FileService fileService;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    public List<DeliveryDto> findAllDeliveryByMember(long id) {
        return deliveryRepository.findAllByMemberId(id, DeliveryDto.class);
    }

    @Transactional
    public Item insertItem(InsertItemDto insertItemDto, Member member) throws IOException {
        Delivery delivery = deliveryRepository.getById(insertItemDto.getDeliveryId());

        Item insertedItem = itemRepository.save(insertItemDto.toEntity(delivery, member));
        List<UploadFile> uploadFiles = fileService.storeFiles(insertItemDto.getItemImageFiles());

        List<ItemImage> itemImages = ItemImage.createItemImages(uploadFiles, insertedItem);
        ItemImage.setMockImageRows(itemImages, insertedItem);

        itemImageRepository.saveAll(itemImages);

        return insertedItem;
    }

    @Transactional
    public UpdateItemDto findItem(Long itemId) {
        return UpdateItemDto.of(findItemById(itemId));
    }

    @Transactional
    public void updateItem(Long itemId, UpdateItemDto updateItemDto) throws IOException {

        // save new images and delete
        List<ItemImage> dbImages = findAllItemById(itemId, ItemImage.class);
        List<UploadFile> uploadFiles = fileService.storeFiles(updateItemDto.getItemImageFiles());
        fileService.deleteImageAfterCompare(updateItemDto, uploadFiles, dbImages);

        // update `item` and 'item-images'
        Delivery delivery = deliveryRepository.getById(updateItemDto.getDeliveryId());
        Item updateItem = updateItemDto.toEntity(delivery, dbImages);
        itemRepository.save(updateItem);
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
    }

    public <T> List<T> findAllItemById(Long itemId, Class<T> clazz) {
        return itemImageRepository.findAllByItemId(itemId, clazz);
    }
}
