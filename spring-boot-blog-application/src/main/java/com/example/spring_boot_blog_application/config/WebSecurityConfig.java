package com.example.spring_boot_blog_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("removal")
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Note: spring security requestMatchers updated again
        // https://stackoverflow.com/questions/76809698/spring-security-method-cannot-decide-pattern-is-mvc-or-not-spring-boot-applicati
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                        auth.requestMatchers(antMatcher("/css/**")).permitAll();
                        auth.requestMatchers(antMatcher("/js/**")).permitAll();
                        auth.requestMatchers(antMatcher("/images/**")).permitAll();
                        auth.requestMatchers(antMatcher("/fonts/**")).permitAll();
                        auth.requestMatchers(antMatcher("/webjars/**")).permitAll();
                        auth.requestMatchers(antMatcher("/")).permitAll();
                        auth.requestMatchers(antMatcher("/rss/**")).permitAll();
                        auth.requestMatchers(antMatcher("/register/**")).permitAll();
                        auth.requestMatchers(antMatcher("/posts/**")).permitAll();
                        auth.requestMatchers(PathRequest.toH2Console()).permitAll();
                        auth.anyRequest().authenticated();
                })

                .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/login?error")
                    .permitAll()
                );

        // TODO: when you move away from h2-console you can remove these        
        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.frameOptions().disable());
        return http.build();
    }
}
