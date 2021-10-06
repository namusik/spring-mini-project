package com.woosik.springminiproject.service;

import com.woosik.springminiproject.dto.CommentDto;
import com.woosik.springminiproject.model.Comment;
import com.woosik.springminiproject.model.Post;
import com.woosik.springminiproject.model.User;
import com.woosik.springminiproject.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(String context, Post post, User user) {
        Comment comment = new Comment(context, user, post);
        return commentRepository.save(comment);
    }
    
    //postId로 commnets 가져오기
    public List<Comment> getComments(Post post) {
        return commentRepository.findAllByPostOrderByModifiedAtDesc(post);
    }

    @Transactional
    public Comment editComment(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("그런 댓글은 없어요")
        );
        comment.update(commentDto);
        return comment;
    }

    public Long deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return commentId;
    }
}
