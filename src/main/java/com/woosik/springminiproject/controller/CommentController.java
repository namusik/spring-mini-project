package com.woosik.springminiproject.controller;

import com.woosik.springminiproject.dto.CommentDto;
import com.woosik.springminiproject.model.Comment;
import com.woosik.springminiproject.model.Post;
import com.woosik.springminiproject.model.User;
import com.woosik.springminiproject.security.UserDetailsImpl;
import com.woosik.springminiproject.service.CommentService;
import com.woosik.springminiproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public CommentController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/comment/{postId}")
    @ResponseBody
    public Comment addComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        System.out.println(commentDto);
        System.out.println(postId);
        Post post = postService.getPost(postId);
        User user = userDetails.getUser();
        String context = commentDto.getContext();

        return commentService.saveComment(context, post, user);
    }

    //댓글 수정
    @PutMapping("/comment/{commentId}")
    @ResponseBody
    public Comment editComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        System.out.println(commentId);
        System.out.println(commentDto);
        return commentService.editComment(commentId, commentDto);
    }

    //댓글삭제
    @DeleteMapping("/comment/{commentId}")
    @ResponseBody
    public Long deleteComment(@PathVariable Long commentId) {
        System.out.println(commentId);
        return commentService.deleteComment(commentId);
    }
}
