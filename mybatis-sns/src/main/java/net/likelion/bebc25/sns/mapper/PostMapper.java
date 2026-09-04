package net.likelion.bebc25.sns.mapper;

import net.likelion.bebc25.sns.dto.PostCreateRequest;
import net.likelion.bebc25.sns.dto.PostDetailResponse;
import net.likelion.bebc25.sns.dto.PostResponse;
import net.likelion.bebc25.sns.dto.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    // 1. 단일 DTO 바인딩 (@Param 생략)
    void save(PostCreateRequest post);

    // 2. 단일 기본형 바인딩 (@Param 명시)
    PostResponse findById(@Param("id") Long id);

    // 3. 단일 기본형 파라미터 기반 목록 조회
    List<PostResponse> findByMemberId(@Param("memberId") Long memberId);

    // 4. 다중 파라미터 바인딩 (@Param 필수)
    void update(@Param("id") Long id, @Param("content") String content, @Param("imageUrl") String imageUrl);

    // 5. 단일 기본형 단건 삭제
    void deleteById(@Param("id") Long id);

    // 복합 ResultMap 조인 상세 조회 (게시글 + 작성자 + 댓글 목록)
    PostDetailResponse findPostDetailById(Long id);

    // 7. 다중 조건 동적 검색 (<where>, <if>)
    List<PostResponse> searchPosts(PostSearchRequest condition);

    // 8. 동적 정렬 분기 조회 (<choose>, <when>, <otherwise>)
    List<PostResponse> findPostsWithSort(PostSearchRequest condition);

    // 9. 동적 부분 수정 (<set>, <if>)
    void updateSelective(Map<String, Object> params);

    // 10. 다중 ID 일괄 삭제 (<foreach>)
    void deleteByIds(@Param("idList") List<Long> idList);

    // 11. 공통 SQL 조각 재사용 조회 (<sql>, <include>)
    PostResponse findByIdWithInclude(@Param("id") Long id);

    // 게시글 좋아요 수 1 증가 (post 테이블)
    void increaseLikeCount(@Param("postId") Long postId);

    // 게시글 좋아요 수 1 감소 (post 테이블)
    void decreaseLikeCount(@Param("postId") Long postId);
}
