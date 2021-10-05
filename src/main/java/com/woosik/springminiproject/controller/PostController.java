package com.woosik.springminiproject.controller;

import com.woosik.springminiproject.domain.Post;
import com.woosik.springminiproject.dto.PostDto;
import com.woosik.springminiproject.repository.PostRepository;
import com.woosik.springminiproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
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
    public Post savePost(@RequestBody PostDto postDto) {
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
    public String findById(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 없습니다")
        );
        System.out.println(post);
        model.addAttribute("post" , post);
        return "detail";
    }
}
