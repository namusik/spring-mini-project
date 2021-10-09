package com.woosik.springminiproject.model;

import com.woosik.springminiproject.dto.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String title;

    @ManyToOne()
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column
    private String content;
//    @OneToMany(mappedBy = "post") //왜 이걸 써야 양방향 매핑이 될까. 객체와 테이블간에 연관관계의 차이를 알아야 한다
//    private List<Comment> comments = new ArrayList<>();

    public Post(String title, User user, String content) {
        this.title = title;
        this.user = user;
        this.content = content;
    }

    public Post(PostDto postDto, User user) {
        this.title = postDto.getTitle();
        this.user = user;
        this.content = postDto.getContent();
    }



    public void update(PostDto postDto) {
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
    }
}
