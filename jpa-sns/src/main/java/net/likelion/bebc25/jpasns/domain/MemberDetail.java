package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {
    @Id
    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private Address address;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(length = 1, nullable = false)
    private String marketingAgreed;

    public MemberDetail(Long memberId, Address address, String introduction) {
        this(memberId, address, introduction, "N");
    }

    public MemberDetail(Long memberId, Address address, String introduction, String marketingAgreed) {
        this.memberId = memberId;
        this.address = address;
        this.introduction = introduction;
        this.marketingAgreed = marketingAgreed;
    }
}
