package com.woosik.springminiproject.repository;

import com.woosik.springminiproject.model.Comment;
import com.woosik.springminiproject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostOrderByModifiedAtDesc(Post post);
}
