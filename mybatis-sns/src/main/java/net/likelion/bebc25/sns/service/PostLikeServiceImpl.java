package net.likelion.bebc25.sns.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.sns.dto.LikeToggleResponse;
import net.likelion.bebc25.sns.dto.PostResponse;
import net.likelion.bebc25.sns.mapper.PostLikeMapper;
import net.likelion.bebc25.sns.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService{
    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;

    @Override
    // readOnly 옵션을 제거하고, 롤백이 가능한 트랜잭션으로 재정의
    @Transactional(rollbackFor = Exception.class)
    public LikeToggleResponse toggleLike(Long memberId, Long postId) {
        LikeToggleResponse likeToggleResponseDto = null;

        // 1. 대상 게시글의 존재 여부 확인
        PostResponse post = postMapper.findById(postId);
        if (post == null) {
            throw new NoSuchElementException("해당 게시글이 존재하지 않습니다. id: " + postId);
        }

        // 2. 현재 사용자의 좋아요 등록 여부 확인
        boolean isLiked = postLikeMapper.countLike(memberId, postId) > 0;

        // 3-1. 이미 등록되어 있을 경우 등록 취소 처리
        if (isLiked) {
            // 좋아요 제거
            postLikeMapper.deleteLike(memberId, postId);
            // 좋아요 수 1 감소
            postMapper.decreaseLikeCount(postId);
            return new LikeToggleResponse(false, post.likeCount() - 1);
        }
        // 3-2. 등록되어 있지 않을 경우 등록 처리
        else {
            // 좋아요 추가
            postLikeMapper.insertLike(memberId, postId);
            // 좋아요 수 1 증가
            postMapper.increaseLikeCount(postId);
            return new LikeToggleResponse(true, post.likeCount() + 1);
        }
    }
}
