package com.shop.projectlion.domain.item.entity;

import com.shop.projectlion.domain.base.BaseEntity;
import com.shop.projectlion.domain.delivery.entity.Delivery;
import com.shop.projectlion.domain.item.type.ItemSellStatus;
import com.shop.projectlion.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item extends BaseEntity {

    @Id
    @Column(length = 20, name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String itemDetail;

    @Column(length = 100)
    private String itemName;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Column(length = 11)
    private int price;

    @Column(length = 11)
    private int stockNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemImage> itemImages;

    @Builder
    public Item(Long id, String itemDetail, String itemName, ItemSellStatus itemSellStatus, int price, int stockNumber,
                Member member, Delivery delivery, List<ItemImage> itemImages) {
        this.id = id;
        this.itemDetail = itemDetail;
        this.itemName = itemName;
        this.itemSellStatus = itemSellStatus;
        this.price = price;
        this.stockNumber = stockNumber;
        this.member = member;
        this.delivery = delivery;
        this.itemImages = itemImages;
    }

    public void updateStockNumber(int newStockNumber) {
        this.stockNumber = newStockNumber;
        this.itemSellStatus = this.stockNumber == 0 ? ItemSellStatus.SOLD_OUT : ItemSellStatus.SELL;
    }
}
