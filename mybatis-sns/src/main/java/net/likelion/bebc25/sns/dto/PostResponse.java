package net.likelion.bebc25.sns.dto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long memberId,
        String content,
        String imageUrl,
        int likeCount,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    // 신규 게시글 등록 요청 DTO로 부터 게시글 응답 DTO를 생성하는 팩토리 메소드
    public static PostResponse from(PostCreateRequest dto) {
        return new PostResponse(
                dto.getId(),
                dto.getMemberId(),
                dto.getContent(),
                dto.getImageUrl(),
                0,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
