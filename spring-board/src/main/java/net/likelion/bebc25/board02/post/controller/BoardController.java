package net.likelion.bebc25.board02.post.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.board02.post.dto.PostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/02/board")
public class BoardController {
    private final List<PostDto> fakePosts;

    public BoardController() {
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

    // 모든 게시글 목록을 반환한다.
    public List<PostDto> getPosts() {
        List<PostDto> result = fakePosts;
        return result;
    }

    // 특정 게시물을 반환한다
    public PostDto getPost(int id) {
        for (PostDto target: getPosts()) {
            if (target.getId() == id) {
                return target;
            }
        }
        throw new IllegalArgumentException(id + "번 게시글은 존재하지 않습니다.");
    }

    // 게시글을 DB에 등록한다
    public void savePost(PostDto post) {
        post.setId(getPosts().getLast().getId() + 1);
        post.setCreatedAt(LocalDateTime.now());
        fakePosts.add(post);
    }

    // 기존 게시글을 수정한다
    public void updatePost(PostDto post) {
        PostDto targetPost = getPost(post.getId());

        if (targetPost != null) {
            targetPost.setTitle(post.getTitle());
            targetPost.setContent(post.getContent());
            targetPost.setAuthor(post.getAuthor());
        }
    }

    // 기존 게시글을 삭제한다
    public void removePost(PostDto post) {
        List<PostDto> posts = getPosts();
        posts.remove(post.getId() - 1);
        for (PostDto targetPost: posts) {
            if (targetPost.getId() > post.getId()) {
                targetPost.setId(targetPost.getId() - 1);
            }
        }
    }

    @GetMapping(value = "/list.html")
    public String getBoardList(Model model) {
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = getPosts();
        model.addAttribute("posts", posts);

        return "board/list";
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model) {
        PostDto post = getPost(id);
        model.addAttribute("post", post);

        // 템플릿 파일 경로
        return "board/detail";
    }

    // 게시글 등록 화면 요청하는 컨트롤러
    @GetMapping("/write.html")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post) { // 모델에 자동으로 주입까지 됨(postDto 이름으로)
        return "board/write";
    }

    // 게시글 수정 화면 요청하는 컨트롤러
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model) {
        PostDto post = getPost(id);
        model.addAttribute("postForm", post);

        return "board/write";
    }

    // 게시글 등록 요청을 처리하는 컨트롤러
    @PostMapping("/new")
    public String writePost(
            @Valid @ModelAttribute("postForm") PostDto post,
            BindingResult bindingResult  // Validation 검증 결과 저장 객체 (대상 객체 바로 뒤에 작성해야 함)
    ) {
        if (bindingResult.hasErrors()) {
            return "board/write";  // 다시 페이지로 돌아가기
        }
        log.debug(post.toString());
        savePost(post);

        // 브라우저에게 list.html로 재요청
        return "redirect:list.html";
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/edit")
    public  String editPost(@Valid @ModelAttribute("postForm") PostDto post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/write";
        }
        log.debug(post.toString());
        updatePost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

//    // 게시글 삭제 요청을 처리하는 컨트롤러 (get)
//    @GetMapping("/delete")
//    public String deletePost(@RequestParam int id) {
//        PostDto target = getPosts().get(id - 1);
//        log.debug(target.toString());
//        removePost(target);
//        return "redirect:list.html";
//    }

    // 게시글 삭제 요청을 처리하는 컨트롤러 (post)
    @PostMapping("/delete")
    public String deletePost(@RequestParam int id) {
        PostDto target = getPost(id);
        log.debug(target.toString());
        removePost(target);
        return "redirect:list.html";
    }


}
