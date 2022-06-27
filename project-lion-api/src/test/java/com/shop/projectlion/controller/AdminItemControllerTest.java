package com.shop.projectlion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.projectlion.api.adminitem.controller.AdminItemController;
import com.shop.projectlion.api.adminitem.service.AdminItemService;
import com.shop.projectlion.api.adminitem.dto.ReadItemDto;
import com.shop.projectlion.api.adminitem.dto.UpdateItemDto;
import com.shop.projectlion.dto.UpdateItemDtoTest;
import com.shop.projectlion.global.error.GlobalExceptionHandler;
import com.shop.projectlion.global.error.exception.BusinessException;
import com.shop.projectlion.global.error.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("상품 Controller Test")
public class AdminItemControllerTest {

    @InjectMocks
    AdminItemController adminItemController;

    @Mock
    AdminItemService adminItemService;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(adminItemController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("상품 조회 API Test")
    void readHandle() throws Exception {
        ReadItemDto mockDto = ReadItemDto.builder().build();

        // given
        given(adminItemService.findItemById(anyLong())).willReturn(mockDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/admin/items/{itemId}", anyLong()).characterEncoding(UTF_8));

        // then
        result.andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(mockDto)));
    }

    @Test
    @DisplayName("상품 수정 API Test")
    void updateHandle() throws Exception {
        // given
        given(adminItemService.updateItemById(anyLong(), any())).willReturn(UpdateItemDtoTest.TEST_DTO);

        // when
        ResultActions result = mockMvc.perform(patch("/api/admin/items/{itemId}", UpdateItemDtoTest.TEST_DTO.getItemId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8) // for print() `request body`
                .content(objectMapper.writeValueAsString(UpdateItemDtoTest.TEST_DTO)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(UpdateItemDtoTest.TEST_DTO)));
    }

    @Test
    @DisplayName("서로 다른 id 값으로 상품 수정 요청")
    void updateItemDifferId() throws Exception {
        UpdateItemDto mock = UpdateItemDto.builder().itemId(1L).build();

        ResultActions result = mockMvc.perform(patch("/api/admin/items/{itemId}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mock)));


        result.andExpect(r -> assertTrue(r.getResolvedException() instanceof BusinessException))
                .andExpect(r -> assertEquals(r.getResolvedException().getMessage(), ErrorCode.NOT_EQUALS_ENTITY_ID.getMessage()));
    }
}
