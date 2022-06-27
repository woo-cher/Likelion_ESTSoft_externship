package com.shop.projectlion.domain.item.repostiory;

import com.shop.projectlion.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM item i JOIN FETCH i.itemImages JOIN FETCH i.member JOIN FETCH i.delivery WHERE i.id = ?1")
    Optional<Item> findByIdWithJoinFetch(Long itemId);

}
