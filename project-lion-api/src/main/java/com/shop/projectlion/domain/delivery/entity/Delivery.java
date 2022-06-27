package com.shop.projectlion.domain.delivery.entity;

import com.shop.projectlion.domain.common.BaseEntity;
import com.shop.projectlion.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "delivery")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery extends BaseEntity {

    @Id
    @Column(length = 20, name = "delivery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11)
    private int deliveryFee;

    @Column(length = 50)
    private String deliveryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Delivery(int deliveryFee, String deliveryName, Member member) {
        this.deliveryFee = deliveryFee;
        this.deliveryName = deliveryName;
        this.member = member;
    }
}
