package com.shop.projectlion.domain.member.repostiory;

import com.shop.projectlion.domain.member.constant.MemberType;
import com.shop.projectlion.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.memberType = :memberType AND m.email = :email")
    Optional<Member> findByMemberTypeAndEmail(@Param("memberType") MemberType memberType, @Param("email") String email);

    Optional<Member> findByEmail(String email);
}
