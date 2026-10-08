package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CommentTest {
    @Autowired
    private EntityManager em;

    Long setUpMemberId;
    Long setUpPostId;
    Long setUpCommentId;

    @BeforeEach
    void setUp() {
        // 1. 회원 등록
        Member member = new Member("haru@test.com", "1234", "하루");
        em.persist(member);
        this.setUpMemberId = member.getId();

        // 2. 게시글 등록
        Post post = new Post(member, "게시글 테스트");
        em.persist(post);
        this.setUpPostId = post.getId();

        // 3. 댓글 등록
        Comment comment = new Comment(member, setUpPostId, "댓글 테스트");
        em.persist(comment);
        this.setUpCommentId = comment.getId();

        em.clear(); // 캐시를 비워 생성된 댓글을 찾기 위함
    }

    @Test
    @DisplayName("댓글 등록")
//    @Commit  // 테스트 종료 후 롤백 시키지 않고 커밋함
    void create() {
        Member member = em.find(Member.class, setUpMemberId);
        Comment comment = new Comment(member, setUpPostId, "새로운 댓글 테스트");
        em.persist(comment);

        em.clear();

        Comment createdComment  = em.find(Comment.class, comment.getId());

        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getId()).isEqualTo(comment.getId());
        assertThat(createdComment.getMember().getId()).isEqualTo(setUpMemberId);
        assertThat(createdComment.getPostId()).isEqualTo(comment.getPostId());
        assertThat(createdComment.getContent()).isEqualTo(comment.getContent());
        assertThat(createdComment.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("댓글 조회")
    void read() {
        Comment comment  = em.find(Comment.class, setUpCommentId);

        // 영속성 컨텍스트의 1차 캐시값에서 즉시 반환되며 select 쿼리를 실행하지 않음
        Comment comment2 = em.find(Comment.class, setUpCommentId);

        // 동일한 주소를 가지고 있는 객체인지 여부
        assertThat(comment).isSameAs(comment2);

        assertThat(comment).isNotNull();
        assertThat(comment.getMember().getId()).isEqualTo(setUpMemberId);
        assertThat(comment.getPostId()).isEqualTo(setUpPostId);
        assertThat(comment.getContent()).isEqualTo("댓글 테스트");
        assertThat(comment.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("댓글 수정")
    void update() {
        Comment comment = em.find(Comment.class, setUpCommentId);

        // 엔티티를 준영속 상태로 전환 -> DB와 엔티티가 분리되어 업데이트가 이루어지지 않음
//        em.detach(comment);

        // 엔티티 속성이 수정되면 update 쿼리가 생성
        // 실행은 바로 되지 않고 버퍼에 저장된 뒤 커밋 직전에 실행됨
        comment.changeContent("수정한 댓글");

        em.flush(); // 버퍼에 저장된 쿼리 DB로 커밋
        em.clear(); // 캐시 비우기

        Comment updatedComment = em.find(Comment.class, setUpCommentId);
        assertThat(updatedComment.getContent()).isEqualTo("수정한 댓글");
    }

    @Test
    @DisplayName("댓글 삭제")
    void delete() {
        Comment comment = em.find(Comment.class, setUpCommentId);
        em.remove(comment);

        em.flush();
        em.clear();

        Comment deletedComment = em.find(Comment.class, setUpCommentId);
        assertThat(deletedComment).isNull();
    }
}
