package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.likelion.bebc25.jpasns.common.entity.BaseTimeEntity;

@Entity
@Table(name = "post_hashtag", uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_hashtag", columnNames = {"post_id", "tag_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHashtag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public PostHashtag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}
