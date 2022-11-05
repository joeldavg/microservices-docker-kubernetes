package com.joeldavg.springcloud.msvc.auth.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @LoadBalanced
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

}
