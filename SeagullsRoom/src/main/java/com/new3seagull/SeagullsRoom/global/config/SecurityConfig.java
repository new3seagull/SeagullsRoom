package com.new3seagull.SeagullsRoom.global.config;

import com.new3seagull.SeagullsRoom.global.jwt.JWTFilter;
import com.new3seagull.SeagullsRoom.global.jwt.JWTUtil;
import com.new3seagull.SeagullsRoom.global.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 시큐리티를 위한 config라는 걸 위해
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
            .csrf((auth) -> auth.disable()); //세션 방식과 달리 jwt는 무상태라 가능
        //세션은 계속 고정되어 있기때문에 막아야 함
        //JWT방식을 이용할 거 가 때문에 아래 2개 disable
        //From 로그인 방식 disable
        http
            .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/rank.html", "/calibration.html", "/mypage.html", "/gpt.html", "/join", "/api/v1/users", "/auth.html", "/login.html", "/calibration.html", "/signup.html", "/main.html", "/h2-console/**", "/favicon.ico", "/css/**", "/js/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/images/**", "/screentime.html").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated());
        //JWTFilter 등록
        http
            .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        // addFilterAt == 추가할 필터, 대체될 필터
        // 로그인 경로 지정
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/v1/login");

        http
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        //세션 설정 세션을 Stateless 상태로 만들어 줘야한다.
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 'X-Frame-Options' 헤더 비활성화 h2 디비에 연결하기 위해
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}