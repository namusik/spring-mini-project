package com.woosik.springminiproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private String context;

    @Override
    public String toString() {
        return "CommentDto{" +
                "context='" + context + '\'' +
                '}';
    }
}
