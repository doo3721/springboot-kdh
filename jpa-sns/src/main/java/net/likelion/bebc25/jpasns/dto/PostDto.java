package net.likelion.bebc25.jpasns.dto;

import net.likelion.bebc25.jpasns.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {
    public record CreateRequest(
            String content
    ) {}

    public record Response(
            Long id,
            String content,
            String authorNickname,
            LocalDateTime createdAt
    ) {
        public static Response from(Post post) {
            return new Response(
                    post.getId(),
                    post.getContent(),
                    post.getMember().getNickname(),
                    post.getCreatedAt()
            );
        }
    }

    public record DetailResponse(
            Long id,
            String content,
            String authorNickname,
            LocalDateTime createdAt,
            List<CommentDto.Response> comments
    ) {
        public static DetailResponse from(Post post) {
            List<CommentDto.Response> comments = post.getComments().stream()
                    .map(CommentDto.Response::from)
                    .toList();

            return new DetailResponse(
                    post.getId(),
                    post.getContent(),
                    post.getMember().getNickname(),
                    post.getCreatedAt(),
                    comments
            );
        }
    }
}
