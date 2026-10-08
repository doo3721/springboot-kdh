package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberPostTest {

    @Autowired
    private EntityManager em;

    private Long setUpMemberId;

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전 독립된 트랜잭션 내에서 기본 회원 생성
        Member member = new Member("test1@test.com", "1234", "테스터1");
        em.persist(member); // insert문 생성
        this.setUpMemberId = member.getId();

        em.flush(); // 쿼리 실행
        em.clear(); // 캐시 삭제
    }

    @Test
    @DisplayName("회원 조회 검증")
    void memberFind() {
        Member findMember = em.find(Member.class, setUpMemberId);

        assertThat(findMember).isNotNull();
        assertThat(findMember.getEmail()).isEqualTo("test1@test.com");
        assertThat(findMember.getNickname()).isEqualTo("테스터1");
        assertThat(findMember.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("게시글 등록 및 조회 검증")
    void postSaveAndFind() {
        // 1. 게시글 생성 및 영속화 (사전 등록된 setUpMemberId 활용)
        Member member = em.find(Member.class, setUpMemberId);
        Post post = new Post(member, "첫 번째 게시글 내용");
        em.persist(post);

        em.flush();
        em.clear();

        // 2. 조회 검증
        Post findPost = em.find(Post.class, post.getId());

        assertThat(findPost).isNotNull();
        assertThat(findPost.getContent()).isEqualTo("첫 번째 게시글 내용");
        assertThat(findPost.getMember().getId()).isEqualTo(setUpMemberId);
        assertThat(findPost.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버 상세 주소 매핑 검증")
    void memberDetailAddressMapping() {
        Address address = new Address("12345", "서울시 강남구 역삼동", "101-202");
        MemberDetail memberDetail = new MemberDetail(setUpMemberId, address, "안녕하세요");
        em.persist(memberDetail);

        em.flush();
        em.clear();

        MemberDetail createdDetail = em.find(MemberDetail.class, memberDetail.getMemberId());

        assertThat(createdDetail).isNotNull();
        assertThat(createdDetail.getAddress().getZipcode()).isEqualTo("12345");
        assertThat(createdDetail.getAddress().getRoadAddress()).isEqualTo("서울시 강남구 역삼동");
        assertThat(createdDetail.getAddress().getDetailAddress()).isEqualTo("101-202");
        assertThat(createdDetail.getIntroduction()).isEqualTo("안녕하세요");
    }

    @Test
    @DisplayName("BaseTimeEntity 상속 동작 확인")
//    @Commit
    void baseTimeAuditing() {
        Member findMember = em.find(Member.class, setUpMemberId);

        LocalDateTime createdAt = findMember.getCreatedAt();
        LocalDateTime updatedAt = findMember.getUpdatedAt();

        assertThat(findMember.getCreatedAt()).isNotNull();
        assertThat(findMember.getUpdatedAt()).isNotNull();

        findMember.changeNickname("테스터2");

        em.flush();
        em.clear();

        Member updatedMember = em.find(Member.class, setUpMemberId);

        System.out.println(updatedMember.getCreatedAt());
        System.out.println(updatedMember.getUpdatedAt());
        assertThat(updatedMember.getNickname()).isEqualTo("테스터2");
        assertThat(updatedMember.getCreatedAt()).isEqualTo(createdAt);
        assertThat(updatedMember.getUpdatedAt()).isNotEqualTo(updatedAt);
    }
}