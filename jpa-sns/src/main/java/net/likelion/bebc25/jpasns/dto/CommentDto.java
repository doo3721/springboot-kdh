package net.likelion.bebc25.jpasns.dto;

import net.likelion.bebc25.jpasns.domain.Comment;

import java.time.LocalDateTime;

public class CommentDto {
    public record CreateRequest(
            String content
    ) {}

    public record Response(
            Long id,
            String content,
            String authorNickname,
            LocalDateTime createdAt
    ) {
        public static Response from(Comment comment) {
            return new Response(
                    comment.getId(),
                    comment.getContent(),
                    comment.getMember().getNickname(),
                    comment.getCreatedAt()
            );
        }
    }
}
