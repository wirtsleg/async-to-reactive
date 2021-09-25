package com.github.wirtsleg.asyncreactive.repository.client;

import com.github.wirtsleg.asyncreactive.dto.Document;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface ReactiveRestHighLevelClient<T extends Document> {
    Mono<Void> index(T doc);

    Mono<Void> indexAll(Collection<T> docs);

    Mono<T> findById(String id);

    Mono<Page<T>> search(SearchSourceBuilder builder);
}
