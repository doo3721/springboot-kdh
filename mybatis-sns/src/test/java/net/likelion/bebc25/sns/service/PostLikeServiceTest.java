package net.likelion.bebc25.sns.service;

import net.likelion.bebc25.sns.dto.LikeToggleResponse;
import net.likelion.bebc25.sns.dto.PostResponse;
import net.likelion.bebc25.sns.mapper.PostLikeMapper;
import net.likelion.bebc25.sns.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Transactional
public class PostLikeServiceTest {
    @Autowired
    private PostLikeService postLikeService;
    @MockitoSpyBean
    // 실제 PostMapper의 동작을 유지하되 특정 메서드만 모킹(Mocking)하여 강제 예외를 주입함
    private PostMapper postMapper;
    @Autowired
    private PostLikeMapper postLikeMapper;

    @RepeatedTest(2)
    @DisplayName("좋아요 토글, 카운트 수 변경 테스트")
    void toggleLikeTest() {
        Long memberId = 1L;
        Long postId = 2L;

        PostResponse beforePost = postMapper.findById(postId);
        boolean beforeLiked = postLikeMapper.countLike(memberId, postId) > 0;

        LikeToggleResponse result = postLikeService.toggleLike(memberId, postId);

        PostResponse afterPost = postMapper.findById(postId);
        boolean afterLiked = postLikeMapper.countLike(memberId, postId) > 0;

        if (beforeLiked) {
            assertThat(result.liked()).isFalse();
            assertThat(result.likeCount()).isEqualTo(beforePost.likeCount() - 1);
            assertThat(afterLiked).isFalse();
            assertThat(afterPost.likeCount()).isEqualTo(beforePost.likeCount() - 1);
        }
        else {
            assertThat(result.liked()).isTrue();
            assertThat(result.likeCount()).isEqualTo(beforePost.likeCount() + 1);
            assertThat(afterLiked).isTrue();
            assertThat(afterPost.likeCount()).isEqualTo(beforePost.likeCount() + 1);
        }
    }

    @Test
    // NOT_SUPPORTED: 진행 중인 부모 트랜잭션을 일시 중단(Suspend)하여 서비스가 독자 트랜잭션으로 즉시 롤백되도록 격리함
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DisplayName("좋아요 토글, 카운트 수 변경 중 예외 발생 테스트")
    void toggleLikeRollbackTest() {
        Long memberId = 1L;
        Long postId = 2L;

        postLikeMapper.deleteLike(memberId, postId);

        // 고의 장애 발생
        doThrow(new RuntimeException("데이터베이스 네트워크 장애 발생"))
                .when(postMapper).increaseLikeCount(postId);

//        LikeToggleResponseDto result = postLikeService.toggleLike(memberId, postId);
        assertThatThrownBy(() -> postLikeService.toggleLike(memberId, postId))
                .isInstanceOf(RuntimeException.class);

//        assertThat(result.liked()).isTrue();
        assertThat(postLikeMapper.countLike(memberId, postId)).isEqualTo(0);
    }
}
