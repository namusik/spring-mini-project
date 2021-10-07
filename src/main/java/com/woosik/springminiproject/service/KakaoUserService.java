package com.woosik.springminiproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woosik.springminiproject.dto.KakaoUserInfoDto;
import com.woosik.springminiproject.model.User;
import com.woosik.springminiproject.model.UserRoleEnum;
import com.woosik.springminiproject.repository.UserRepository;
import com.woosik.springminiproject.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class KakaoUserService {
    private final UserRepository userRepository;
    //BCrypt 해시함수가 passwordEncoder를 구현한거여서
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void kakaologin(String code) throws JsonProcessingException { //throws 이걸 호출한 애한테 예외처리 넘김
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakadoUserInfo(accessToken);

        // 3. 카카오 사용자 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. 카카오 유저 강제 로그인 처리
        forcedLoginUser(kakaoUser);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "e3a17b237cdce208b79b90b0f284e01c");
        body.add("redirect_uri", "http://localhost:8086/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        //요청을 보내서 response로 받음 body부분에 json 형태
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakadoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        //응답이 또 JSON 형탠
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        String responseBody = response.getBody();
        responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        System.out.println("카카오 사용자 정보: " + id + ", " + nickname);
        return new KakaoUserInfoDto(id, nickname);
    }

    @Transactional
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // 서버에서 카카오아이디를 가져와서
        // DB에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            //가입된 카카오유저가 없다면

            // 회원가입 진행
            // username: kakao nickname
            String nickname = kakaoUserInfo.getNickname();

            // password: random UUID 랜덤수생성
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            // role: 일반 사용자
            UserRoleEnum role = UserRoleEnum.USER;

            kakaoUser = new User(nickname, encodedPassword, role, kakaoId);
            userRepository.save(kakaoUser);

        }
        return kakaoUser;
    }

    private void forcedLoginUser(User kakaoUser) {
        //감싸주기
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //contextholder로 set해서 넣어주기
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
