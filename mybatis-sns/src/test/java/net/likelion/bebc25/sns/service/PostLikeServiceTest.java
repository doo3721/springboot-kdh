package net.likelion.bebc25.sns.service;

import net.likelion.bebc25.sns.dto.LikeToggleResponseDto;
import net.likelion.bebc25.sns.dto.PostResponseDto;
import net.likelion.bebc25.sns.mapper.PostLikeMapper;
import net.likelion.bebc25.sns.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Transactional
public class PostLikeServiceTest {
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostLikeMapper postLikeMapper;

    @RepeatedTest(2)
    @DisplayName("좋아요 토글, 카운트 수 변경 테스트")
    void toggleLikeTest() {
        Long memberId = 1L;
        Long postId = 2L;

        PostResponseDto beforePost = postMapper.findById(postId);
        boolean beforeLiked = postLikeMapper.countLike(memberId, postId) > 0;

        LikeToggleResponseDto result = postLikeService.toggleLike(memberId, postId);

        PostResponseDto afterPost = postMapper.findById(postId);
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
    @DisplayName("좋아요 토글, 카운트 수 변경 중 예외 발생 테스트")
    void toggleLikeRollbackTest() {
        Long memberId = 1L;
        Long postId = 2L;

        doThrow(new RuntimeException("데이터베이스 네트워크 장애 발생")).when(postMapper).increaseLikeCount(postId);

        LikeToggleResponseDto result = postLikeService.toggleLike(memberId, postId);
    }
}
