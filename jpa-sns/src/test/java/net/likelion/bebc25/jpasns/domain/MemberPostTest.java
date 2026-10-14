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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Member member = em.find(Member.class, setUpMemberId);
        MemberDetail memberDetail = new MemberDetail(member, address, "안녕하세요");
        em.persist(memberDetail);

        em.flush();
        em.clear();

        MemberDetail createdDetail = em.find(MemberDetail.class, setUpMemberId);

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

    @Test
    @DisplayName("회원 상세 1:1 식별 관계(@MapsId) 매핑 검증")
    void memberDetailMapsIdMapping() {
        // 1. 회원 조회 및 임베디드 주소 생성
        Member member = em.find(Member.class, setUpMemberId);
        Address address = new Address("06234", "서울특별시 강남구 테헤란로 123", "4층 401호");

        // 2. 회원 상세 정보 생성 (연관된 Member 객체 지정)
        MemberDetail memberDetail = new MemberDetail(member, address, "안녕하세요. 백엔드 개발자입니다.");
        em.persist(memberDetail);

        em.flush();
        em.clear();

        // 3. 회원 상세 단건 조회 검증
        MemberDetail findDetail = em.find(MemberDetail.class, setUpMemberId);
        assertThat(findDetail).isNotNull();
        assertThat(findDetail.getId()).isEqualTo(setUpMemberId); // 부모 PK와 자식 PK 일치 검증 (@MapsId)
        assertThat(findDetail.getIntroduction()).isEqualTo("안녕하세요. 백엔드 개발자입니다.");

        // 4. 회원 상세를 통한 회원 단방향 객체 탐색 검증 (1:1)
        assertThat(findDetail.getMember()).isNotNull();
        assertThat(findDetail.getMember().getId()).isEqualTo(setUpMemberId);
        assertThat(findDetail.getMember().getEmail()).isEqualTo("test1@test.com");
    }

    @Test
    @DisplayName("게시글-태그 연결 엔티티 등록 및 다대일 객체 탐색 검증")
    void postHashtagSaveAndGraphTraversal() {
        // 1. 작성자 회원 및 게시글 생성
        Member member = em.find(Member.class, setUpMemberId);
        Post post = new Post(member, "해시태그 테스트 게시글");
        em.persist(post);

        // 2. 독립 Tag 엔티티 생성 및 영속화
        Tag springTag = new Tag("SpringBoot");
        Tag jpaTag = new Tag("JPA");
        em.persist(springTag);
        em.persist(jpaTag);

        // 3. 다대다 연결 엔티티(PostHashtag) 생성 및 영속화
        PostHashtag postHashtag1 = new PostHashtag(post, springTag);
        PostHashtag postHashtag2 = new PostHashtag(post, jpaTag);
        em.persist(postHashtag1);
        em.persist(postHashtag2);

        em.flush();
        em.clear();

        // 4. 연결 엔티티 조회를 통한 다대일 단방향 객체 탐색 검증
        PostHashtag findHashtag = em.find(PostHashtag.class, postHashtag1.getId());
        assertThat(findHashtag).isNotNull();
        assertThat(findHashtag.getPost().getId()).isEqualTo(post.getId());
        assertThat(findHashtag.getPost().getContent()).isEqualTo("해시태그 테스트 게시글");
        assertThat(findHashtag.getTag().getId()).isEqualTo(springTag.getId());
        assertThat(findHashtag.getTag().getName()).isEqualTo("SpringBoot");
    }

    @Test
    @DisplayName("동일 게시글에 동일 태그 중복 연결 시 복합 유니크 제약조건 위반 검증")
    void duplicatePostHashtagThrowsException() {
        // 1. 게시글 및 태그 생성
        Member member = em.find(Member.class, setUpMemberId);
        Post post = new Post(member, "중복 태그 테스트 게시글");
        em.persist(post);

        Tag tag = new Tag("Java");
        em.persist(tag);

        // 2. 첫 번째 해시태그 연결 등록
        PostHashtag firstHashtag = new PostHashtag(post, tag);
        em.persist(firstHashtag);
        em.flush();

        // 3. 동일한 게시글에 동일한 태그로 두 번째 연결 등록 시도 시 예외 발생 검증
        PostHashtag duplicateHashtag = new PostHashtag(post, tag);

        // IDENTITY 전략은 PK 채번을 위해 em.persist() 호출 즉시 INSERT 쿼리가 실행되므로 persist 시점에 복합 유니크 위반 예외 발생
        assertThatThrownBy(() -> {
            em.persist(duplicateHashtag);
            em.flush();
        }).isInstanceOf(Exception.class);
    }
}