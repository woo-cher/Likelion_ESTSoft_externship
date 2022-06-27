package com.shop.projectlion.domain.delivery.repository;

import com.shop.projectlion.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    <T> List<T> findAllByMemberId(long memberId, Class<T> clazz);
}
