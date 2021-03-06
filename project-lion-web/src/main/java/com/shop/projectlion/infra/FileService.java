package com.shop.projectlion.infra;

import com.shop.projectlion.domain.item.entity.ItemImage;
import com.shop.projectlion.web.adminitem.dto.UpdateItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${file.upload.path}")
    private String fileUploadPath;

    public String getFullFileUploadPath(String filename) {
        return fileUploadPath + filename;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fileUploadUrl = getFullFileUploadPath(storeFileName);
        multipartFile.transferTo(new File(getFullFileUploadPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName, fileUploadUrl);
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

    public void deleteFile(String fileUploadUrl) {
        if (fileUploadUrl == null) {
            return;
        }

        File deleteFile = new File(fileUploadUrl);
        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("????????? ?????????????????????.");
        } else {
            log.info("????????? ???????????? ????????????.");
        }
    }

    public void deleteImageAfterCompare(UpdateItemDto updateItemDto, List<UploadFile> uploadFiles, List<ItemImage> itemImages) {
        int idx = 0;
        Iterator<UploadFile> iterator = uploadFiles.iterator();

        for (MultipartFile file : updateItemDto.getItemImageFiles()) {
            ItemImage itemImage = itemImages.get(idx);

            if (!file.isEmpty()) {
                deleteFile(itemImage.getImageUrl());

                if (iterator.hasNext()) {
                    itemImages.set(idx, ItemImage.getModifyItemImage(itemImage, iterator.next()));
                }
            }

            String newImg = updateItemDto.getOriginalImageNames().get(idx);
            String dbImg = itemImage.getOriginalImageName();

            // delete image request
            if (newImg.isBlank() && dbImg != null) {
                deleteFile(itemImage.getImageUrl());
                itemImage.clearImage();
            }

            idx++;
        }
    }
}
