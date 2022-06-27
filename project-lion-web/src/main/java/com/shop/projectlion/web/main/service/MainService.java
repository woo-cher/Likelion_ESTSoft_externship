package com.shop.projectlion.web.main.service;

import com.shop.projectlion.domain.item.repostiory.ItemRepository;
import com.shop.projectlion.web.main.dto.MainItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final ItemRepository ItemRepository;

    public Page<MainItemDto> findAllMainItemDtos(String searchQuery, Pageable pageable) {
        return ItemRepository.findAllBySearchQuery(searchQuery, pageable);
    }
}
