package com.github.wirtsleg.asyncreactive.repository.client.lisneter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.MonoSink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SearchActionListener<T> implements ActionListener<SearchResponse> {

    private final MonoSink<Page<T>> sink;
    private final ObjectMapper mapper;
    private final Class<T> cls;
    private final int from;
    private final int size;

    @Override
    public void onResponse(SearchResponse res) {
        try {
            if (res.status().getStatus() < 400) {
                List<T> docs = readDocuments(res);
                Pageable pageable = PageRequest.of(from / size, size);

                sink.success(new PageImpl<>(docs, pageable, res.getHits().getTotalHits().value));
                return;
            }

            sink.error(new RuntimeException(res.toString()));
        } catch (IOException e) {
            sink.error(e);
        }
    }

    @Override
    public void onFailure(Exception e) {
        sink.error(e);
    }


    private List<T> readDocuments(SearchResponse res) throws IOException {
        List<T> result = new ArrayList<>();

        for (SearchHit hit : res.getHits())
            result.add(mapper.readValue(hit.getSourceAsString(), cls));

        return result;
    }
}
