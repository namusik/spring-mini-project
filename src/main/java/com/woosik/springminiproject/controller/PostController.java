package com.woosik.springminiproject.controller;

import com.woosik.springminiproject.model.Comment;
import com.woosik.springminiproject.model.Post;
import com.woosik.springminiproject.dto.PostDto;
import com.woosik.springminiproject.model.User;
import com.woosik.springminiproject.repository.PostRepository;
import com.woosik.springminiproject.security.UserDetailsImpl;
import com.woosik.springminiproject.service.CommentService;
import com.woosik.springminiproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostRepository postRepository, PostService postService, CommentService commentService) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.commentService = commentService;
    }

    //메인페이지
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if(userDetails == null){
            return "index";
        }else{
            model.addAttribute("nickname", userDetails.getUsername());
            return "index";
        }
    }

    //전체 게시물 불러오기
    @GetMapping("/api/posts")
    @ResponseBody
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc();
    }

    // 제목 검색 게시물 가져오기
    @GetMapping("/api/search/{searchword}/{index}")
    @ResponseBody
    public List<Post> searchByTitle(@PathVariable("searchword") String searchword, @PathVariable("index") String index) {
        return postService.search(index, searchword);
    }

    //게시물 저장하기
    @PostMapping("/api/posts")
    @ResponseBody
    public Post savePost(@RequestBody PostDto postDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(postDto);
        postDto.setNickname(userDetails.getUsername());
        Post post = new Post(postDto);
        System.out.println(post);
        return postRepository.save(post);
    }

    //게시물 수정하기
    @PutMapping("/api/posts/{id}")
    @ResponseBody
    public Long editPost(@PathVariable Long id, @RequestBody PostDto postDto) {
        return postService.update(id, postDto);
    }

    //게시물 삭제하기
    @DeleteMapping("/api/posts/{id}")
    @ResponseBody
    public Long deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return id;
    }


    //게시물 작성하러 가기
    @GetMapping("/writeform")
    public String writeForm() {
        return "writeForm";
    }

    //게시물 상세보기
    @GetMapping("/api/posts/{id}")
    public String findById(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 없습니다")
        );
        System.out.println(post);
        model.addAttribute("post" , post);
        
        //해당 post의 comment 가져오기
        List<Comment> comments = commentService.getComments(post);
        model.addAttribute("comments", comments);

        if(userDetails != null){
            String nickname = userDetails.getUsername();
            User user = userDetails.getUser();
            model.addAttribute("nickname", nickname);
            model.addAttribute("user", user);
        }
        return "detail";
    }
}
