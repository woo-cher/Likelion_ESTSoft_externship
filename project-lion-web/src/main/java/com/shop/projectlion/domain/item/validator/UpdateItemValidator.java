package com.shop.projectlion.domain.item.validator;

import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.web.adminitem.dto.InsertItemDto;
import com.shop.projectlion.web.adminitem.dto.UpdateItemDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class UpdateItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(InsertItemDto.class);
    }

    /**
     *  1. 파일은 안 건들고 수정
     *   - 파일 네임은 존재, 파일 리스트는 비어있음 (no-error)
     *
     *  2. 파일을 삭제만하고 수정
     *   - 파일 네임이 빈 값, 파일 리스트는 비어있음 (error)
     *
     *  3. 파일을 대체하고 수정
     *   - 파일 네임은 존재, 파일 리스트에 파일 존재 (no-error)
     */
    @Override
    public void validate(Object target, Errors errors) {
        UpdateItemDto dto = (UpdateItemDto) target;
        List<String> originalFileNames = dto.getOriginalImageNames();
        List<MultipartFile> multipartFiles = dto.getItemImageFiles();

        if (originalFileNames.get(0).isBlank()) {
            if (!multipartFiles.get(0).isEmpty()) {
                return;
            }

            ErrorCode e = ErrorCode.EMPTY_REPRESENTATION_IMAGE;
            errors.reject(e.name(), e.getMessage());
        }
    }
}
