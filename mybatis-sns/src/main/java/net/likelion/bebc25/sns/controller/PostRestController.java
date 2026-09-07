package net.likelion.bebc25.sns.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.sns.dto.PostCreateRequest;
import net.likelion.bebc25.sns.dto.PostResponse;
import net.likelion.bebc25.sns.dto.PostSearchRequest;
import net.likelion.bebc25.sns.dto.PostUpdateRequest;
import net.likelion.bebc25.sns.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostRestController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPostList(
            @ModelAttribute PostSearchRequest searchRequest
    ) {
        // 검색어에 해당하는 게시글 목록 조회, 빈 검색어이므로 모든 게시글 조회
        List<PostResponse> posts = postService.searchPosts(searchRequest);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("X-Member-Id") Long memberId,
            @Valid @RequestBody PostCreateRequest createRequest
    ) {
        createRequest.setMemberId(memberId);
        PostResponse createdPost = postService.createPost(createRequest);
        // 좀 더 RESTful 한 개념
        URI location = URI.create("/api/v1/posts/" + createdPost.id());
        return ResponseEntity.created(location).body(createdPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable("id") Long postId
    ) {
        PostResponse post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @RequestHeader("X-Member-Id") Long memberId,
            @PathVariable("id") Long postId,
            @Valid @RequestBody PostUpdateRequest updateRequest
    ) {
        PostResponse post = postService.getPostById(postId);
        if (!post.memberId().equals(memberId)) {
            throw new IllegalStateException("본인의 게시글만 수정이 가능합니다.");
        }
        postService.updatePost(postId, updateRequest);
        PostResponse updatedPost = postService.getPostById(postId);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @RequestHeader("X-Member-Id") Long memberId,
            @PathVariable("id") Long postId
    ) {
        PostResponse post = postService.getPostById(postId);
        if (!post.memberId().equals(memberId)) {
            throw new IllegalStateException("본인의 게시글만 삭제가 가능합니다.");
        }
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
