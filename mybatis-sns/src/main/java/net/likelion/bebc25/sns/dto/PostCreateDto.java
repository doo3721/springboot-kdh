package net.likelion.bebc25.sns.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostCreateDto {
    private Long id;
    private Long memberId;
    private String content;
    private String imageUrl;

    public PostCreateDto(Long memberId, String content, String imageUrl) {
        this.memberId = memberId;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}