package com.woosik.springminiproject.domain;

import com.woosik.springminiproject.dto.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String username;
    @Column
    private String password;
    @Column
    private String content;

    public Post(String title, String username, String content, String password) {
        this.title = title;
        this.username = username;
        this.content = content;
        this.password = password;
    }

    public Post(PostDto postDto) {
        this.title = postDto.getTitle();
        this.username = postDto.getUsername();
        this.content = postDto.getContent();
        this.password = postDto.getPassword();
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void update(PostDto postDto) {
        this.title = postDto.getTitle();
        this.username = postDto.getUsername();
        this.content = postDto.getContent();
        this.password = postDto.getPassword();
    }
}
