package com.joeldavg.springcloud.msvc.usuarios.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/authorized").permitAll()
                .antMatchers(HttpMethod.GET, "/", "/{id}").hasAnyAuthority("SCOPE_READ", "SCOPE_WRITE")
                .antMatchers(HttpMethod.POST, "/").hasAuthority("SCOPE_WRITE")
                .antMatchers(HttpMethod.PUT, "/{id}").hasAuthority("SCOPE_WRITE")
                .antMatchers(HttpMethod.DELETE, "/{id}").hasAuthority("SCOPE_WRITE")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-users-client"))
                .oauth2Client(withDefaults())
                .oauth2ResourceServer().jwt();

        return http.build();
    }

}
