package net.likelion.bebc25.sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostCreateRequest {
    private Long id;

//    @NotNull(message = "작성자 회원 번호는 필수 항목입니다.")
    private Long memberId;

    @NotBlank(message = "본문 내용은 필수입니다.")
    @Size(max = 1000, message = "본문은 1000자 이하여야 합니다.")
    private String content;
    private String imageUrl;

    public PostCreateRequest(Long memberId, String content, String imageUrl) {
        this.memberId = memberId;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}