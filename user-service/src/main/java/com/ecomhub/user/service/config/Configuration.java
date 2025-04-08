package com.ecomhub.user.service.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@EnableScheduling
public class Configuration {

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}
