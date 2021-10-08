package com.woosik.springminiproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.woosik.springminiproject.dto.UserDto;
import com.woosik.springminiproject.model.User;
import com.woosik.springminiproject.security.UserDetailsImpl;
import com.woosik.springminiproject.service.KakaoUserService;
import com.woosik.springminiproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {

        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    //회원가입 페이지 이동
    @GetMapping("user/signup")
    public String signupForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        try {
            User user = userDetails.getUser();
            model.addAttribute("user", user);
        }catch (NullPointerException e){
            return "signup";
        }
        return "signup";
    }

    //회원가입
    @ResponseBody
    @PostMapping("user/signup")
    public User signUp(@RequestBody UserDto userDto) {
        User user = userService.signup(userDto);
        System.out.println(user);
        return user;
    }

    @ResponseBody
    @GetMapping("user/nicknamecheck")
    public User nicknameCheck(@RequestBody String nickname_give) {
        System.out.println(nickname_give);
        return userService.nicknameCheck(nickname_give);
    }

    //로그인 페이지 이동
    @GetMapping("user/login")
    public String loginForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        try {
            User user = userDetails.getUser();
            model.addAttribute("user", user);
        }catch (NullPointerException e){
            model.addAttribute("user", "");
            return "login";
        }
        return "login";
    }
    
    //카카오 로그인 인가 처리 URI
    @GetMapping("/user/kakao/callback")
    public String kakaologin(@RequestParam String code) throws JsonProcessingException {
        //authorizeCode : 카카오 서버로 부터 받은 인가 코드
        kakaoUserService.kakaologin(code);
        //서비스에서 다 처리 끝나면 홈으로 리다이렉트 그전에 값을 가져온 거를 로그인 처리 해줘야함
        return "redirect:/";
    }

    @PostMapping("user/check_dup")
    @ResponseBody
    public String nicknameDuplicate(@ModelAttribute("username_give") String username) {
        return userService.findByNickname(username);
    }

}
