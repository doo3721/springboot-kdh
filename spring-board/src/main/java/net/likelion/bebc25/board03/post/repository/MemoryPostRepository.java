package net.likelion.bebc25.board03.post.repository;

import net.likelion.bebc25.board03.post.dto.PostDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemoryPostRepository implements PostRepository {
    private final List<PostDto> fakePosts;

    public MemoryPostRepository() {
        PostDto post1 = new PostDto();
        post1.setId(1);
        post1.setTitle("1번 게시글");
        post1.setContent("1번 게시글 내용입니다.");
        post1.setAuthor("하루");
        post1.setSecret(true);
        post1.setCreatedAt(LocalDateTime.now());

        PostDto post2 = new PostDto();
        post2.setId(2);
        post2.setTitle("2번 게시글");
        post2.setContent("2번 게시글 내용입니다.");
        post2.setAuthor("나무");
        post2.setCreatedAt(LocalDateTime.now());

        fakePosts = new ArrayList<PostDto>();
        fakePosts.add(post1);
        fakePosts.add(post2);
    }

    @Override
    public List<PostDto> findAll() {
        return fakePosts;
    }

    @Override
    public PostDto findById(int id) {
        for (PostDto target: fakePosts) {
            if (target.getId() == id) {
                return target;
            }
        }
        throw new IllegalArgumentException(id + "번 게시글은 존재하지 않습니다.");
    }

    @Override
    public void save(PostDto post) {
        post.setId(fakePosts.getLast().getId() + 1);
        post.setCreatedAt(LocalDateTime.now());
        fakePosts.add(post);
    }

    @Override
    public void update(PostDto post) {
        PostDto targetPost = findById(post.getId());

        if (targetPost != null) {
            targetPost.setTitle(post.getTitle());
            targetPost.setContent(post.getContent());
            targetPost.setAuthor(post.getAuthor());
        }
    }

    @Override
    public void deleteById(int id) {
        // 대상 글 삭제
        fakePosts.remove(id - 1);

        // 나머지 글 id 정렬
        for (PostDto post: fakePosts) {
            if (post.getId() > id) {
                post.setId(post.getId() - 1);
            }
        }
    }
}
