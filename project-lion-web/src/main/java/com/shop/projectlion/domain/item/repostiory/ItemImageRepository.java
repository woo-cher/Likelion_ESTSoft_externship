package com.shop.projectlion.domain.item.repostiory;

import com.shop.projectlion.domain.item.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    <T> List<T> findAllByItemId(Long itemId, Class<T> clazz);
}
