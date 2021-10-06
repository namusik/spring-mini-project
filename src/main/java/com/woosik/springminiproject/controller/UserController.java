package com.woosik.springminiproject.controller;

import com.woosik.springminiproject.dto.UserDto;
import com.woosik.springminiproject.model.User;
import com.woosik.springminiproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입 페이지 이동
    @GetMapping("user/signup")
    public String signupForm() {
        return "signup";
    }

    //회원가입
    @PostMapping("user/signup")
    public String signUp(UserDto userDto) {
        User user = userService.signup(userDto);
        System.out.println(user);
        return "login";
    }

    @ResponseBody
    @GetMapping("user/nicknamecheck")
    public User nicknameCheck(@RequestBody String nickname_give) {
        System.out.println(nickname_give);
        return userService.nicknameCheck(nickname_give);
    }

    //로그인 페이지 이동
    @GetMapping("user/login")
    public String loginForm() {
        return "login";
    }


}
