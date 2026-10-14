package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostLikeTest {
    @Autowired
    private EntityManager em;

    private Long setUpMemberId;
    private Long setUpPostId;

    @BeforeEach
    void setUp() {
        Member member = new Member("haru@test.com", "1234", "하루");
        em.persist(member);

        Post post = new Post(member, "테스트");
        em.persist(post);

        this.setUpMemberId = member.getId();
        this.setUpPostId = post.getId();

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("좋아요 등록과 조회")
    void postLikeSaveAndFind() {
        Member member = em.find(Member.class, setUpMemberId);
        Post post = em.find(Post.class, setUpPostId);

        PostLike postLike = new PostLike(member, post);
        em.persist(postLike);

        em.flush();
        em.clear();

        PostLike createdPostLike = em.find(PostLike.class, postLike.getId());

        assertThat(createdPostLike).isNotNull();
        assertThat(createdPostLike.getMember().getId()).isEqualTo(setUpMemberId);
        assertThat(createdPostLike.getMember().getNickname()).isEqualTo(member.getNickname());

        assertThat(createdPostLike.getPost().getId()).isEqualTo(setUpPostId);
        assertThat(createdPostLike.getPost().getContent()).isEqualTo(post.getContent());

        assertThat(createdPostLike.getCreatedAt()).isNotNull();
        assertThat(createdPostLike.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("동일 회원의 동일 게시글에 대한 좋아요 중복 검증")
    void duplicatePostLikeThrowException() {
        // 1. 게시글 및 태그 생성
        Member member = em.find(Member.class, setUpMemberId);
        Post post = new Post(member, "중복 태그 테스트 게시글");
        em.persist(post);

        // 2. 좋아요 생성
        PostLike postLike = new PostLike(member, post);
        em.persist(postLike);

        em.flush();
        em.clear();

        // 3. 동일 회원의 동일 게시글을 두 번째 좋아요 등록 시도 시 예외 발생 검증
        PostLike duplicatePostLike = new PostLike(member, post);

        // IDENTITY 전략은 PK 채번을 위해 em.persist() 호출 즉시 INSERT 쿼리가 실행되므로 persist 시점에 복합 유니크 위반 예외 발생
        assertThatThrownBy(() -> {
            em.persist(duplicatePostLike);
            em.flush();
        }).isInstanceOf(Exception.class);
    }
}
