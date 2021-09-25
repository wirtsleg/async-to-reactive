package com.github.wirtsleg.asyncreactive.repository;

import com.github.wirtsleg.asyncreactive.dto.Event;
import com.github.wirtsleg.asyncreactive.dto.EventSearchParams;
import com.github.wirtsleg.asyncreactive.repository.client.ReactiveRestHighLevelClient;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

@Repository
@RequiredArgsConstructor
public class EventRepository {
    private final ReactiveRestHighLevelClient<Event> evtClient;

    public Mono<Void> index(Event evt) {
        return evtClient.index(evt);
    }

    public Mono<Void> indexAll(Collection<Event> events) {
        return evtClient.indexAll(events);
    }

    public Mono<Event> findById(String id) {
        return evtClient.findById(id);
    }

    public Mono<Page<Event>> search(EventSearchParams params) {
        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(wildcardQuery("name", params.getQuery()))
                .from(params.getFrom())
                .size(params.getSize());

        return evtClient.search(builder);
    }
}
