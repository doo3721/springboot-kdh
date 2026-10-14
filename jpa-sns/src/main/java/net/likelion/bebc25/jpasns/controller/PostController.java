package net.likelion.bebc25.jpasns.controller;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.jpasns.dto.CommentDto;
import net.likelion.bebc25.jpasns.dto.PostDto;
import net.likelion.bebc25.jpasns.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostDto.Response> createPost (
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestBody PostDto.CreateRequest request
    ) {
        PostDto.Response response = postService.createPost(memberId, request);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + response.id())).body(response);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDto.DetailResponse> getPost(
            @PathVariable Long id
    ) {
        PostDto.DetailResponse response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    // 댓글 작성
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto.Response> addComment(
            @PathVariable("id") Long postId,
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestBody CommentDto.CreateRequest request
    ) {
        CommentDto.Response response = postService.addComment(postId, memberId, request);
        return ResponseEntity.created(
                URI.create("/api/v1/posts/" + postId + "/comments/" + response.id())
        ).body(response);
    }

    // 게시글 목록 최신순 조회
    // memberId 있으면 해당 회원의 게시글만, 없으면 전체 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostDto.Response>> getPosts(
            @RequestParam(required = false) Long memberId
    ) {
        if (memberId == null) {
            List<PostDto.Response> posts = postService.getPosts();
            return ResponseEntity.ok(posts);
        }
        List<PostDto.Response> posts = postService.getPostsByMember(memberId);
        return ResponseEntity.ok(posts);
    }
}
