package net.likelion.bebc25.jpasns.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.likelion.bebc25.jpasns.common.entity.BasePostReaction;
import net.likelion.bebc25.jpasns.common.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_like", uniqueConstraints = {
        @UniqueConstraint(name = "uk_post_like_member_post", columnNames = {"member_id", "post_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BasePostReaction {
    public PostLike(Member member, Post post) {
        super(member, post);
    }
}
