package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail {
    @Id
    @Column(name = "member_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Address address;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(length = 1, nullable = false)
    private String marketingAgreed;

    public MemberDetail(Member member, Address address, String introduction) {
        this(member, address, introduction, "N");
    }

    public MemberDetail(Member member, Address address, String introduction, String marketingAgreed) {
        this.member = member;
        this.address = address;
        this.introduction = introduction;
        this.marketingAgreed = marketingAgreed;
    }
}
