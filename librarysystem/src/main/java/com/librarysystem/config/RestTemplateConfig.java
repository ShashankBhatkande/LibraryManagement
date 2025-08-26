package com.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        restTemplate.getInterceptors().add((request, body, execution) -> {
            String token = TokenUtils.getCurrentToken();
            if(token != null) {
                request.getHeaders().add("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
