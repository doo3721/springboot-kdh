package net.likelion.bebc25.jpasns.repository;

import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 회원이 작성한 게시글 목록을 최신 등록 순으로 조회
    List<Post> findByMemberOrderByCreatedAtDesc(Member member);

    // 전체 게시글 목록을 최신 등록순으로 정렬 조회
    List<Post> findAllByOrderByCreatedAtDesc();

    // 회원 식별자(PK)를 조건으로 게시글 목록 최신순 조회
    List<Post> findByMember_IdOrderByCreatedAtDesc(Long memberId);
}
