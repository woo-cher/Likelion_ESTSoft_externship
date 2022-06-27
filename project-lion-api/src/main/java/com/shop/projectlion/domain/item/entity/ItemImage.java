package com.shop.projectlion.domain.item.entity;

import com.shop.projectlion.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

}
