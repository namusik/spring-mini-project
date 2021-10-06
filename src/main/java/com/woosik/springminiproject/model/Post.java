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
    @Column
    private String nickname;
    @Column
    private String content;
//    @OneToMany(mappedBy = "post") //왜 이걸 써야 양방향 매핑이 될까. 객체와 테이블간에 연관관계의 차이를 알아야 한다
//    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String nickname, String content, String password) {
        this.title = title;
        this.nickname = nickname;
        this.content = content;
    }

    public Post(PostDto postDto) {
        this.title = postDto.getTitle();
        this.nickname = postDto.getNickname();
        this.content = postDto.getContent();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void update(PostDto postDto) {
        this.title = postDto.getTitle();
        this.nickname = postDto.getNickname();
        this.content = postDto.getContent();
    }
}
