package com.woosik.springminiproject.model;

import com.woosik.springminiproject.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String context;

//    @Column(nullable = false)
//    private Long userNickname;
    @ManyToOne()
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne() //여러개의 댓글이 하나의 포스트에
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    public Comment(String context, User user, Post post) {
        this.context = context;
        this.user = user;
        this.post = post;
    }

    public Comment(CommentDto commentDto) {
        this.context = commentDto.getContext();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", context='" + context + '\'' +
                ", user=" + user +
                ", post=" + post +
                '}';
    }

    public void update(CommentDto commentDto) {
        this.context = commentDto.getContext();
    }
}
