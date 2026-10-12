package dev.kunal.productcatalogservice_aug26.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced // this will ensure the requests are in Round Robin Manner to any service
    // line to achieve client-side load balancing
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
