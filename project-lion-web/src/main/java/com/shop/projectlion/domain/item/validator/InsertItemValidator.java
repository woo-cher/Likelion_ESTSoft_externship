package com.shop.projectlion.domain.item.validator;

import com.shop.projectlion.global.error.exception.ErrorCode;
import com.shop.projectlion.web.adminitem.dto.InsertItemDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class InsertItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(InsertItemDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InsertItemDto dto = (InsertItemDto) target;
        List<MultipartFile> multipartFiles = dto.getItemImageFiles();

        if (!multipartFiles.isEmpty()) {
            if (multipartFiles.get(0).isEmpty()) {
                ErrorCode e = ErrorCode.EMPTY_REPRESENTATION_IMAGE;
                errors.reject(e.name(), e.getMessage());
            }
        }
    }
}
