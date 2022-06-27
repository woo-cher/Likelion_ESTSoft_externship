package com.shop.projectlion.domain.item.entity;

import com.shop.projectlion.domain.base.BaseEntity;
import com.shop.projectlion.global.constant.NumberConstant;
import com.shop.projectlion.global.constant.StringConstant;
import com.shop.projectlion.infra.UploadFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "item_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemImage extends BaseEntity {

    @Id
    @Column(length = 20, name = "item_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String imageName;

    @Column(length = 500)
    private String imageUrl;

    private boolean isRepImage;

    @Column(length = 200)
    private String originalImageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public ItemImage(Long id, String imageName, String imageUrl, boolean isRepImage, String originalImageName, Item item) {
        this.id = id;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.isRepImage = isRepImage;
        this.originalImageName = originalImageName;
        this.item = item;
    }

    public static List<ItemImage> createItemImages(List<UploadFile> uploadFiles, Item item) {
        List<ItemImage> itemImages = new ArrayList<>();

        for (UploadFile file : uploadFiles) {
            ItemImage itemImage = ItemImage.builder()
                    .isRepImage(itemImages.isEmpty())
                    .item(item)
                    .originalImageName(file.getOriginalFileName())
                    .imageName(file.getStoreFileName())
                    .imageUrl(StringConstant.IMAGE_REQUEST_PREFIX.value() + file.getStoreFileName())
                    .build();

            itemImages.add(itemImage);
        }

        return itemImages;
    }

    public static ItemImage getModifyItemImage(ItemImage itemImage, UploadFile uploadFile) {
        return ItemImage.builder()
                .id(itemImage.getId())
                .isRepImage(itemImage.isRepImage())
                .item(itemImage.getItem())
                .originalImageName(uploadFile.getOriginalFileName())
                .imageName(uploadFile.getStoreFileName())
                .imageUrl(StringConstant.IMAGE_REQUEST_PREFIX.value() + uploadFile.getStoreFileName())
                .item(itemImage.getItem())
                .build();
    }

    public static void setMockImageRows(List<ItemImage> itemImages, Item item) {
        while (itemImages.size() != NumberConstant.ITEM_IMAGE_MAX_COUNT.value()) {
            itemImages.add(ItemImage.builder().item(item).build());
        }
    }

    public void clearImage() {
        this.imageName = null;
        this.imageUrl = null;
        this.originalImageName = null;
    }
}
