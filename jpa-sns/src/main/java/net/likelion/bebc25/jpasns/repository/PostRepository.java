package net.likelion.bebc25.jpasns.repository;

import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.domain.Post;
import net.likelion.bebc25.jpasns.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 회원이 작성한 게시글 목록을 최신 등록 순으로 조회
    List<Post> findByMemberOrderByCreatedAtDesc(Member member);

    // 전체 게시글 목록을 최신 등록순으로 정렬 조회
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 회원 식별자(PK)를 조건으로 게시글 목록 최신순 조회
    Page<Post> findByMember_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    // 회원 식별자(PK)를 조건으로 게시글 목록 최신순 조회
    Page<Post> findByMember_Id(Long memberId, Pageable pageable);

    // 전체 게시글 목록을 키워드 포함 최신 등록순으로 정렬 조회
    List<Post> findByContentContainingOrderByCreatedAtDesc(String keyword);

    // 회원 식별자(PK)를 조건으로 본문에 키워드 포함 검색 최신 등록순으로 정렬 조회
    List<Post> findByMember_IdAndContentContainingOrderByCreatedAtDesc(Long memberId, String keyword);

    // 회원 식별자(PK)를 조건으로 본문에 키워드 포함 검색 최신 등록순으로 정렬 조회
    Page<Post> findByMember_IdAndContentContaining(Long memberId, String keyword, Pageable pageable);

    @Query("""
        SELECT p
        FROM Post p
        JOIN p.member m
        WHERE m.id = :memberId
            AND p.content LIKE %:keyword%
    """)
    Page<Post> findByMemberAndKeyword(
            @Param("memberId") Long memberId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // 본문 키워드 포함 검색
    Page<Post> findByContentContaining(String keyword, Pageable pageable);

    // 특정 좋아요 수 이상의 인기 게시글 조회
    List<Post> findByLikeCountGreaterThanEqual(int minLikeCount);

    // 특정 회원의 특정 좋아요 수 이상 게시글 최신순 정렬 조회 (조건 누적으로 메서드명이 길어진 쿼리 메서드)
    List<Post> findByMember_IdAndLikeCountGreaterThanEqualOrderByCreatedAtDesc(
            Long memberId,
            int minLikeCount
    );

    @Query("""
        SELECT p
        FROM Post p
        JOIN p.member m
        WHERE m.id = :memberId
            AND p.likeCount >= :minLikeCount
        ORDER BY p.createdAt DESC
    """)
    List<Post> findByMemberAndMinLike(
            @Param("memberId") Long memberId,
            @Param("minLikeCount") int minLikeCount
    );

    @Query("""
        SELECT new net.likelion.bebc25.jpasns.dto.PostDto$SummaryResponse(
            p.id, m.nickname, p.content, p.likeCount
        )
        FROM Post p
        JOIN p.member m
        WHERE m.id = :memberId
""")
    List<PostDto.SummaryResponse> findSummariesByMemberId(
            @Param("memberId") Long memberId
    );
}
