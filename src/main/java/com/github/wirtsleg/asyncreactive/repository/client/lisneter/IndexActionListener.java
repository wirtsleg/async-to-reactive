package com.github.wirtsleg.asyncreactive.repository.client.lisneter;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import reactor.core.publisher.MonoSink;

@RequiredArgsConstructor
public class IndexActionListener<T> implements ActionListener<IndexResponse> {

    private final MonoSink<T> sink;

    @Override
    public void onResponse(IndexResponse res) {
        if (res.status().getStatus() < 400) {
            sink.success();
            return;
        }

        sink.error(new RuntimeException(res.toString()));
    }

    @Override
    public void onFailure(Exception e) {
        sink.error(e);
    }
}
