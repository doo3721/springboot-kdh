package net.likelion.bebc25.sns.mapper;

import net.likelion.bebc25.sns.dto.PostCreateDto;
import net.likelion.bebc25.sns.dto.PostDetailResponseDto;
import net.likelion.bebc25.sns.dto.PostResponseDto;
import net.likelion.bebc25.sns.dto.PostSearchCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    // 1. 단일 DTO 바인딩 (@Param 생략)
    void save(PostCreateDto post);

    // 2. 단일 기본형 바인딩 (@Param 명시)
    PostResponseDto findById(@Param("id") Long id);

    // 3. 단일 기본형 파라미터 기반 목록 조회
    List<PostResponseDto> findByMemberId(@Param("memberId") Long memberId);

    // 4. 다중 파라미터 바인딩 (@Param 필수)
    void update(@Param("id") Long id, @Param("content") String content, @Param("imageUrl") String imageUrl);

    // 5. 단일 기본형 단건 삭제
    void deleteById(@Param("id") Long id);

    // 복합 ResultMap 조인 상세 조회 (게시글 + 작성자 + 댓글 목록)
    PostDetailResponseDto findPostDetailById(Long id);

    // 7. 다중 조건 동적 검색 (<where>, <if>)
    List<PostResponseDto> searchPosts(PostSearchCondition condition);

    // 8. 동적 정렬 분기 조회 (<choose>, <when>, <otherwise>)
    List<PostResponseDto> findPostsWithSort(PostSearchCondition condition);

    // 9. 동적 부분 수정 (<set>, <if>)
    void updateSelective(Map<String, Object> params);

    // 10. 다중 ID 일괄 삭제 (<foreach>)
    void deleteByIds(@Param("idList") List<Long> idList);

    // 11. 공통 SQL 조각 재사용 조회 (<sql>, <include>)
    PostResponseDto findByIdWithInclude(@Param("id") Long id);
}
