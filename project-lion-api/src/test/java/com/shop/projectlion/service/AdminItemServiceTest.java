package com.shop.projectlion.service;

import com.shop.projectlion.api.adminitem.service.AdminItemService;
import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.delivery.service.DeliveryService;
import com.shop.projectlion.api.adminitem.dto.UpdateItemDto;
import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.item.repostiory.ItemRepository;
import com.shop.projectlion.dto.UpdateItemDtoTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("상품 Service Test")
public class AdminItemServiceTest {

    @InjectMocks
    AdminItemService adminItemService;

    @Mock
    ItemRepository itemRepository;

    @Mock
    DeliveryService deliveryService;

    @Test
    @DisplayName("상품 수정 로직 Test")
    void updateService() {
        Item mockItem = Item.builder().build();
        Delivery mockDelivery = Delivery.builder().build();

        // given
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(mockItem));
        given(deliveryService.findDeliveryById(UpdateItemDtoTest.TEST_DTO.getDeliveryId())).willReturn(mockDelivery);

        // when
        UpdateItemDto updatedItemDto = adminItemService.updateItemById(1L, UpdateItemDtoTest.TEST_DTO);

        // then
        assertThat(updatedItemDto).isEqualTo(UpdateItemDtoTest.TEST_DTO);
    }
}
