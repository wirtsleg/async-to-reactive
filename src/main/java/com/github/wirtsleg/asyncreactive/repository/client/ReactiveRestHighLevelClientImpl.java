package com.github.wirtsleg.asyncreactive.repository.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wirtsleg.asyncreactive.dto.Document;
import com.github.wirtsleg.asyncreactive.repository.client.lisneter.BulkIndexActionListener;
import com.github.wirtsleg.asyncreactive.repository.client.lisneter.GetActionListener;
import com.github.wirtsleg.asyncreactive.repository.client.lisneter.IndexActionListener;
import com.github.wirtsleg.asyncreactive.repository.client.lisneter.SearchActionListener;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collection;

import static org.elasticsearch.client.RequestOptions.DEFAULT;

@RequiredArgsConstructor
public class ReactiveRestHighLevelClientImpl<T extends Document> implements ReactiveRestHighLevelClient<T> {
    private final RestHighLevelClient client;
    private final ObjectMapper mapper;
    private final Class<T> cls;
    private final String index;

    @Override
    public Mono<Void> index(T doc) {
        IndexRequest req = indexRequest(doc);

        return Mono.create(sink -> client.indexAsync(req, RequestOptions.DEFAULT, new IndexActionListener<>(sink)));
    }

    @Override
    public Mono<Void> indexAll(Collection<T> docs) {
        BulkRequest req = new BulkRequest();

        docs.forEach(doc -> req.add(indexRequest(doc)));

        return Mono.create(sink -> client.bulkAsync(req, DEFAULT, new BulkIndexActionListener<>(sink)));
    }

    @Override
    public Mono<T> findById(String id) {
        GetRequest req = new GetRequest(index, id);

        return Mono.create(sink -> client.getAsync(req, DEFAULT, new GetActionListener<>(sink, mapper, cls)));
    }

    @Override
    public Mono<Page<T>> search(SearchSourceBuilder builder) {
        SearchRequest req = new SearchRequest(index)
                .source(builder);

        return Mono.create(sink ->
            client.searchAsync(req, DEFAULT, new SearchActionListener<T>(sink, mapper, cls, builder.from(), builder.size()))
        );
    }

    private IndexRequest indexRequest(T doc) {
        try {
            return new IndexRequest(index)
                    .id(doc.getId())
                    .source(mapper.writeValueAsString(doc), XContentType.JSON);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
