package com.shop.projectlion.domain.item.repostiory;

import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.web.itemdtl.dto.ItemDtlDto;
import com.shop.projectlion.web.main.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT new com.shop.projectlion.web.main.dto.MainItemDto(i.id, i.itemName, i.itemDetail, it.imageUrl, i.price) " +
            "FROM item i JOIN item_image it ON i.id = it.item.id " +
            "WHERE i.itemSellStatus = 'SELL' AND it.isRepImage = true " +
            "AND (i.itemName LIKE %:query% OR i.itemDetail LIKE %:query%)")
    Page<MainItemDto> findAllBySearchQuery(@Param("query") String searchQuery, Pageable pageable);

    @Query("SELECT new com.shop.projectlion.web.itemdtl.dto.ItemDtlDto(i.id, i.itemName, i.price, i.itemDetail," +
            " i.stockNumber, i.itemSellStatus, d.deliveryFee) " +
            "FROM item i JOIN delivery d ON i.delivery.id = d.id " +
            "WHERE i.id = :itemId")
    Optional<ItemDtlDto> findItemDtlDtoById(@Param("itemId") Long itemId);
}
