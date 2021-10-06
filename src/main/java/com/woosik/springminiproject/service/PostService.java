package com.woosik.springminiproject.service;

import com.woosik.springminiproject.model.Post;
import com.woosik.springminiproject.dto.PostDto;
import com.woosik.springminiproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long update(Long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 게시물이 없습니다.")
        );
        post.update(postDto);
        return id;
    }

    public List<Post> search(String index, String searchword) {
        if (index.equals("제목")) {
            return postRepository.findByTitleContainingOrderByModifiedAtDesc(searchword);
        } else if (index.equals("작성자")) {
            return postRepository.findByNicknameContainingOrderByModifiedAtDesc(searchword);
        } else {
            return postRepository.findByContentContainingOrderByModifiedAtDesc(searchword);
        }
    }

    //특정 post 찾아오기
    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }
}