package net.likelion.bebc25.sns.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostLikeMapperTest {
    @Autowired
    private PostLikeMapper postLikeMapper;

    @Test
    @DisplayName("좋아요 등록 및 등록 여부 조회")
    void insertAndCountLikeTest() {
        Long memberId = 1L;
        Long postId = 2L;

        int beforeCount = postLikeMapper.countLike(memberId, postId);
        postLikeMapper.insertLike(memberId, postId);
        int afterCount = postLikeMapper.countLike(memberId, postId);

        assertThat(beforeCount).isEqualTo(0);
        assertThat(afterCount).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    void deleteLikeTest() {
        Long memberId = 1L;
        Long postId = 10L;
        postLikeMapper.insertLike(memberId, postId);

        int beforeCount = postLikeMapper.countLike(memberId, postId);
        postLikeMapper.deleteLike(memberId, postId);
        int afterCount = postLikeMapper.countLike(memberId, postId);

        assertThat(beforeCount).isEqualTo(1);
        assertThat(afterCount).isEqualTo(0);
    }
}