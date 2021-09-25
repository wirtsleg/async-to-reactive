package com.github.wirtsleg.asyncreactive.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wirtsleg.asyncreactive.dto.Event;
import com.github.wirtsleg.asyncreactive.repository.client.ReactiveRestHighLevelClient;
import com.github.wirtsleg.asyncreactive.repository.client.ReactiveRestHighLevelClientImpl;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    public static final String EVENT_INDEX = "events";

    @Bean
    public ReactiveRestHighLevelClient<Event> eventReactiveRestHighLevelClient(
            RestHighLevelClient client,
            ObjectMapper mapper
    ) {
        return new ReactiveRestHighLevelClientImpl<>(client, mapper, Event.class, EVENT_INDEX);
    }
}
