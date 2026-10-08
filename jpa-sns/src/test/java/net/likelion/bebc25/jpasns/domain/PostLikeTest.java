package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Member findMember = em.find(Member.class, setUpMemberId);
        Post findPost = em.find(Post.class, setUpPostId);

        PostLike postLike = new PostLike(findMember, findPost);
        em.persist(postLike);

        em.flush();
        em.clear();

        PostLike createdPostLike = em.find(PostLike.class, postLike.getId());

        assertThat(createdPostLike.getMember().getId()).isEqualTo(setUpMemberId);
        assertThat(createdPostLike.getPost().getId()).isEqualTo(setUpPostId);
    }
}
