package com.github.wirtsleg.asyncreactive.repository.client.lisneter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.ResourceNotFoundException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetResponse;
import reactor.core.publisher.MonoSink;

import java.io.IOException;

@RequiredArgsConstructor
public class GetActionListener<T> implements ActionListener<GetResponse> {

    private final MonoSink<T> sink;
    private final ObjectMapper mapper;
    private final Class<T> cls;

    @Override
    public void onResponse(GetResponse res) {
        try {
            if (res.isExists()) {
                T resultDocument = mapper.readValue(res.getSourceAsString(), cls);

                sink.success(resultDocument);
                return;
            }

            sink.error(new ResourceNotFoundException(res.toString()));
        } catch (IOException e) {
            sink.error(e);
        }
    }

    @Override
    public void onFailure(Exception e) {
        sink.error(e);
    }
}
