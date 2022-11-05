package com.joeldavg.springcloud.msvc.auth.services;

import com.joeldavg.springcloud.msvc.auth.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private WebClient client;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = client.get()
                    .uri("http://msvc-users:8001/login", uri -> uri.queryParam("email", email).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();

            LOGGER.info("User login: " + user.getEmail());
            LOGGER.info("User name: " + user.getName());
            LOGGER.info("User password: " + user.getPassword());

            return new org.springframework.security.core.userdetails.User(email,
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        } catch (RuntimeException e) {
            String error = "Error en login, no existe el usuario en el sistema " + email;
            LOGGER.error(error);
            LOGGER.error(e.getMessage());
            throw new UsernameNotFoundException(error);
        }
    }

}
