package com.mycompany.myteam.config;

import com.mycompany.myteam.config.filter.UserNameLoggingFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
                .csrf().disable()
            .authorizeRequests()
            .antMatchers("/actuator/**", "/h2-console/**")
                .permitAll()
            .anyRequest()
                .authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(STATELESS)
            .and()
                .headers().frameOptions().sameOrigin()
            .and()
                .addFilterAfter(new UserNameLoggingFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "*"
        ));
        configuration.setAllowedMethods(List.of("POST"));
        configuration.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
