package me.dragonappear.infra.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
public class CustomWebSecurityCustomizer implements WebSecurityCustomizer {

    @Override
    public void customize(WebSecurity web) {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .rememberMe().disable()
                .csrf().disable()
                .sessionManagement(getSessionManagementConfigurer());

        return http.build();
    }

    private Customizer<SessionManagementConfigurer<HttpSecurity>> getSessionManagementConfigurer() {
        return (SessionManagementConfigurer) -> SessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .sessionFixation().changeSessionId();
    }

}
