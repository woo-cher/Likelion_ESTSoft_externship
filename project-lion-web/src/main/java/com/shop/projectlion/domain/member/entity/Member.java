package com.shop.projectlion.domain.member.entity;

import com.shop.projectlion.domain.base.BaseEntity;
import com.shop.projectlion.domain.item.entity.Item;
import com.shop.projectlion.domain.member.type.MemberRole;
import com.shop.projectlion.domain.member.type.MemberType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @Column(length = 50, name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String memberName;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private MemberType memberType;

    @Column(length = 200)
    private String password;

    @Column(length = 250)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MemberRole role;

    @Column(length = 6)
    private LocalDateTime tokenExpirationTime;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Item> items;

    @Builder
    public Member(String email, String memberName, MemberType memberType, String password, String refreshToken,
                  MemberRole role, LocalDateTime tokenExpirationTime, List<Item> items) {
        this.email = email;
        this.memberName = memberName;
        this.memberType = memberType;
        this.password = password;
        this.refreshToken = refreshToken;
        this.role = role;
        this.tokenExpirationTime = tokenExpirationTime;
        this.items = items;
    }
}
