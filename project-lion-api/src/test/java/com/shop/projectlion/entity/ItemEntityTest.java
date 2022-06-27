package com.shop.projectlion.entity;

import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.item.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 Entity Test")
public class ItemEntityTest {

    @Test
    @DisplayName("상품 수정 메소드 Test")
    void updateItemByItem() {

        // given
        Item item = Item.builder()
                .itemName("name")
                .price(1000)
                .itemDetail("detail")
                .stockNumber(100)
                .delivery(Delivery.builder().build())
                .build();

        Item updateItem = Item.builder()
                .itemName("newName")
                .price(2000)
                .itemDetail("newDetail")
                .stockNumber(200)
                .delivery(Delivery.builder().build())
                .build();

        // when
        item.updateByItem(updateItem);

        // then
        assertThat(item.getItemName()).isEqualTo(updateItem.getItemName());
        assertThat(item.getPrice()).isEqualTo(updateItem.getPrice());
        assertThat(item.getItemDetail()).isEqualTo(updateItem.getItemDetail());
        assertThat(item.getStockNumber()).isEqualTo(updateItem.getStockNumber());
        assertThat(item.getDelivery()).isEqualTo(updateItem.getDelivery());
    }
}
