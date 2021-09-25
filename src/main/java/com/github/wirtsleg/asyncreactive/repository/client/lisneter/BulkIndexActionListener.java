package com.github.wirtsleg.asyncreactive.repository.client.lisneter;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkResponse;
import reactor.core.publisher.MonoSink;

@RequiredArgsConstructor
public class BulkIndexActionListener<T> implements ActionListener<BulkResponse> {

    private final MonoSink<T> sink;

    @Override
    public void onResponse(BulkResponse res) {
        if (res.hasFailures()) {
            sink.error(new RuntimeException(res.buildFailureMessage()));
            return;
        }

        sink.success();
    }

    @Override
    public void onFailure(Exception e) {
        sink.error(e);
    }
}
