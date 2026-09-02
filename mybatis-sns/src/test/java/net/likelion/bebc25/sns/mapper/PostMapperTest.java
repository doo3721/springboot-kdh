package net.likelion.bebc25.sns.mapper;

import net.likelion.bebc25.sns.dto.PostCreateDto;
import net.likelion.bebc25.sns.dto.PostDetailResponseDto;
import net.likelion.bebc25.sns.dto.PostResponseDto;
import net.likelion.bebc25.sns.dto.PostSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostMapperTest {

    @Autowired
    private PostMapper postMapper;

    @Test
    @DisplayName("게시글 한 건 조회 테스트")
    void findByIdTest() {
        PostResponseDto foundPost = postMapper.findById(1L);
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.id()).isEqualTo(1L);

        PostResponseDto notFoundPost = postMapper.findById(999999999L);
        assertThat(notFoundPost).isNull();
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void saveTest() {
        PostCreateDto newPost = new PostCreateDto(1L, "신규 게시글 등록 테스트", null);

        postMapper.save(newPost);

        List<PostResponseDto> memberPosts = postMapper.findByMemberId(1L);

        assertThat(memberPosts).isNotEmpty();

        PostResponseDto lastPost = memberPosts.getFirst();
        assertThat(lastPost.content()).isEqualTo(newPost.getContent());
        assertThat(lastPost.memberId()).isEqualTo(newPost.getMemberId());
    }

    @Test
    @DisplayName("작성자 ID 기반 게시글 목록 조회 테스트")
    void findByMemberIdTest() {
        // given: 1번 회원 ID 기준 조회
        Long targetMemberId = 1L;

        // when: 해당 회원의 게시글 목록 조회
        List<PostResponseDto> posts = postMapper.findByMemberId(targetMemberId);

        // then: 조회된 모든 게시글의 memberId가 1번인지 검증
        assertThat(posts).isNotNull();
        for (PostResponseDto post : posts) {
            assertThat(post.memberId()).isEqualTo(targetMemberId);
        }
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updateTest() {
        // given: 1번 게시글 대상 수정할 본문과 이미지 준비
        Long targetPostId = 1L;
        String updatedContent = "수정 완료된 게시글 본문입니다.";
        String updatedImageUrl = "https://image.com/updated.jpg";

        // when: 게시글 수정 실행
        postMapper.update(targetPostId, updatedContent, updatedImageUrl);

        // then: 단건 조회 후 수정된 내용이 정상 반영되었는지 검증
        PostResponseDto updatedPost = postMapper.findById(targetPostId);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.content()).isEqualTo(updatedContent);
        assertThat(updatedPost.imageUrl()).isEqualTo(updatedImageUrl);
    }

    @Test
    @DisplayName("게시글 단건 삭제 테스트")
    void deleteByIdTest() {
        // given: 1번 회원의 신규 게시글을 먼저 등록하고 생성된 ID 확인
        PostCreateDto post = new PostCreateDto(1L, "삭제될 임시 게시글", null);
        postMapper.save(post);

        List<PostResponseDto> posts = postMapper.findByMemberId(1L);
        Long targetPostId = posts.getFirst().id();

        // when: 단건 삭제 실행
        postMapper.deleteById(targetPostId);

        // then: 삭제 후 단건 조회 시 null이 반환되는지 검증
        PostResponseDto deletedPost = postMapper.findById(targetPostId);
        assertThat(deletedPost).isNull();
    }

    @Test
    @DisplayName("ResultMap 3중 조인 상세 조회(게시글 + 작성자 + 댓글 목록) 테스트")
    void findPostDetailByIdTest() {
        // given: 1번 게시글 조회 (schema.sql / data.sql 기준)
        Long targetPostId = 1L;

        // when: 3중 조인 상세 조회 실행
        PostDetailResponseDto detail = postMapper.findPostDetailById(targetPostId);

        // then: 복합 매핑 객체 정합성 검증
        if (detail != null) {
            System.out.println(detail);
            assertThat(detail.getId()).isEqualTo(targetPostId);
            assertThat(detail.getContent()).isNotNull();

            // 1:1 작성자 요약 객체 매핑 검증 (<association>)
            assertThat(detail.getAuthor()).isNotNull();
            assertThat(detail.getAuthor().id()).isNotNull();
            assertThat(detail.getAuthor().nickname()).isNotNull();

            // 1:N 댓글 목록 컬렉션 매핑 검증 (<collection>)
            assertThat(detail.getComments()).isNotNull();
        }
    }

    @Test
    @DisplayName("동적 SQL 키워드 검색 (<where>, <if>) 테스트")
    void searchPostsWithKeywordTest() {
        // given: 테스트용 게시글 등록
        postMapper.save(new PostCreateDto(1L, "동적 SQL 검색용 키워드 스프링부트", null));

        PostSearchCondition condition = new PostSearchCondition("스프링부트", null, null, null);

        // when: 키워드 동적 검색 실행
        List<PostResponseDto> searchResults = postMapper.searchPosts(condition);

        // then: 검색된 모든 게시글 본문에 키워드가 포함되어 있는지 검증
        assertThat(searchResults).isNotEmpty();
        for (PostResponseDto post : searchResults) {
            assertThat(post.content()).contains("스프링부트");
        }
    }

    @Test
    @DisplayName("동적 SQL 작성자 ID 및 다중 타겟 ID IN 절 검색 (<foreach>) 테스트")
    void searchPostsWithTargetMemberIdsTest() {
        // given: 1번 회원 게시글 등록
        postMapper.save(new PostCreateDto(1L, "다중 ID 검색 대상 게시글", null));

        List<Long> targetIds = List.of(1L);
        PostSearchCondition condition = new PostSearchCondition(null, null, targetIds, null);

        // when: 다중 회원 ID 대상 IN 절 동적 검색 실행
        List<PostResponseDto> searchResults = postMapper.searchPosts(condition);

        // then: 검색 결과의 모든 작성자가 대상 회원 목록에 포함되는지 검증
        assertThat(searchResults).isNotEmpty();
        for (PostResponseDto post : searchResults) {
            assertThat(targetIds).contains(post.memberId());
        }
    }

    @Test
    @DisplayName("동적 SQL 정렬 분기 (<choose>, <when>, <otherwise>) 테스트")
    void findPostsWithSortTest() {
        // given: 정렬 조건 생성
        PostSearchCondition condition = new PostSearchCondition(null, null, null, "POPULAR");

        // when: 정렬 분기 쿼리 실행
        List<PostResponseDto> posts = postMapper.findPostsWithSort(condition);

        // then: 결과 반환 검증
        assertThat(posts).isNotNull();
    }

    @Test
    @DisplayName("동적 SQL 부분 수정 (<set>, <if>) 테스트")
    void updateSelectiveTest() {
        // given: 1번 게시글 대상 본문만 선택적으로 수정하는 파라미터 맵 구성
        Long targetPostId = 1L;
        String newContent = "동적 set 태그를 통한 본문 단독 수정";

        Map<String, Object> params = new HashMap<>();
        params.put("id", targetPostId);
        params.put("content", newContent);
        // imageUrl은 누락(null)하여 UPDATE 대상에서 제외

        // when: 동적 부분 수정 실행
        postMapper.updateSelective(params);

        // then: 본문만 정상 변경되었는지 검증
        PostResponseDto updatedPost = postMapper.findById(targetPostId);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.content()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("동적 SQL 다중 ID 일괄 삭제 (<foreach>) 테스트")
    void deleteByIdsTest() {
        // given: 삭제할 게시글 등록 후 ID 확보
        PostCreateDto post1 = new PostCreateDto(1L, "일괄 삭제 대상 게시글 1", null);
        PostCreateDto post2 = new PostCreateDto(1L, "일괄 삭제 대상 게시글 2", null);
        postMapper.save(post1);
        postMapper.save(post2);

        List<PostResponseDto> memberPosts = postMapper.findByMemberId(1L);
        Long id1 = memberPosts.get(0).id();
        Long id2 = memberPosts.get(1).id();
        List<Long> targetIds = List.of(id1, id2);

        // when: 다중 ID 일괄 삭제 실행
        postMapper.deleteByIds(targetIds);

        // then: 삭제된 대상 ID들이 단건 조회 시 모두 null로 반환되는지 검증
        for (Long id : targetIds) {
            assertThat(postMapper.findById(id)).isNull();
        }
    }

    @Test
    @DisplayName("공통 SQL 조각 재사용 (<sql>, <include>) 단건 조회 테스트")
    void findByIdWithIncludeTest() {
        // given: 1번 게시글 조회 ID
        Long targetPostId = 1L;

        // when: include 태그를 활용한 단건 조회 실행
        PostResponseDto foundPost = postMapper.findByIdWithInclude(targetPostId);

        // then: 조회 데이터 검증
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.id()).isEqualTo(targetPostId);
        assertThat(foundPost.content()).isNotNull();
    }
}
