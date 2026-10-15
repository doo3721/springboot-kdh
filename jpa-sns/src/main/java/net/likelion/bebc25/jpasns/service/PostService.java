package net.likelion.bebc25.jpasns.service;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.jpasns.domain.Comment;
import net.likelion.bebc25.jpasns.domain.Member;
import net.likelion.bebc25.jpasns.domain.Post;
import net.likelion.bebc25.jpasns.dto.CommentDto;
import net.likelion.bebc25.jpasns.dto.PostDto;
import net.likelion.bebc25.jpasns.repository.CommentRepository;
import net.likelion.bebc25.jpasns.repository.MemberRepository;
import net.likelion.bebc25.jpasns.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostDto.Response createPost(Long memberId, PostDto.CreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId));
        Post post = new Post(member, request.content());

        Post savedPost = postRepository.save(post);
        return PostDto.Response.from(savedPost);
    }

    public PostDto.DetailResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다. id = " + id));
        return PostDto.DetailResponse.from(post);
    }

    @Transactional
    public CommentDto.Response addComment(Long postId, Long memberId, CommentDto.CreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다. id = " + postId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId));
        Comment comment = new Comment(member, request.content());

        // 영속성 컨텍스트의 동기화와 Comment에 post 정보를 추가하기 위해 필요
        post.addComment(comment);

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.Response.from(savedComment);
    }

    // 전체 게시글 목록 조회 (페이징)
    public Page<PostDto.Response> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostDto.Response::from);
    }

    // 전체 게시글 목록 조회 (조건 포함)
    public Page<PostDto.Response> getPosts(Long memberId, String keyword, Pageable pageable) {
        Page<Post> posts;

        if (memberId != null) {
            if (!memberRepository.existsById(memberId)) {
                throw new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId);
            }

            posts = keyword != null
                    ? postRepository.findByMember_IdAndContentContaining(memberId, keyword, pageable)
//                    ? postRepository.findByMemberAndKeyword(memberId, keyword, pageable)
                    : postRepository.findByMember_Id(memberId, pageable);
        }
        else {
            posts = keyword != null ?
                    postRepository.findByContentContaining(keyword, pageable)
                    : postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        return posts.map(PostDto.Response::from);
    }

    // 특정 회원이 작성한 게시글 목록 최신순 조회
//    public List<PostDto.Response> getPostsByMember(Long memberId) {
//        if (!memberRepository.existsById(memberId)) {
//            throw new IllegalArgumentException("존재하지 않는 회원입니다. id = " + memberId);
//        }
//
//        return postRepository.findByMember_IdOrderByCreatedAtDesc(memberId).stream()
//                .map(PostDto.Response::from)
//                .toList();
//    }
}
