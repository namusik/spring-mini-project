package com.woosik.springminiproject.repository;

import com.woosik.springminiproject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();

    List<Post> findByTitleContainingOrderByModifiedAtDesc(String title);

    List<Post> findByNicknameContainingOrderByModifiedAtDesc(String username);

    List<Post> findByContentContainingOrderByModifiedAtDesc(String content);

}
