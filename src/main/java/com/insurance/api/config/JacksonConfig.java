package com.insurance.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // Register Hibernate5JakartaModule to handle lazy-loaded entities
        Hibernate5JakartaModule hibernateModule = new Hibernate5JakartaModule();

        // Configure serialization of lazy-loaded objects
        // This will ignore lazy-loaded properties instead of trying to serialize them
        hibernateModule.enable(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING);

        objectMapper.registerModule(hibernateModule);

        return objectMapper;
    }
}