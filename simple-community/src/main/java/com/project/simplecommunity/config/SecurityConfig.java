package com.project.simplecommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers().permitAll()
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll()   // 일단 전부 허용, 이후에 변경
                )
                .formLogin(withDefaults())  // url 은 /login 이고 username, password 필드가 존재
                .logout(logout -> logout.logoutSuccessUrl("/"))
                //.csrf(csrf -> csrf.ignoringAntMatchers("/**"))
                .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
//        return username -> userAccountService
//                .searchUser(username)
//                .map(UserPrincipal::from)
//                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
