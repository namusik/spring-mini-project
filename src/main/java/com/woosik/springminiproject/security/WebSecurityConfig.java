package com.woosik.springminiproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    //h2-console 사용에 대한 허용 (CSRF, FrameOption 무시)
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2-console/**");
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();

        http.authorizeRequests()
                .antMatchers("/", "/user/**", "/images/**", "/css/**","/api/posts","/api/posts/{id}","/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll() //index.html 허용, userController 허용
                .anyRequest().authenticated();

        http.formLogin() //로그인 관련 설정
                .loginPage("/user/login") //로그인 view 페이지 따로 설정 GET /user/login
                .loginProcessingUrl("/user/login") //로그인처리 Post/user/login
                .defaultSuccessUrl("/")  //로그인성공하면 "/"로 이동
                .failureUrl("/user/login?error") //로그인실패시 view
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .permitAll();

    }
}
