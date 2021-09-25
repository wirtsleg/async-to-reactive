package com.github.wirtsleg.asyncreactive.web;

import com.github.wirtsleg.asyncreactive.dto.Event;
import com.github.wirtsleg.asyncreactive.dto.EventSearchParams;
import com.github.wirtsleg.asyncreactive.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepo;

    @GetMapping("/{id}")
    public Mono<Event> getEvent(@PathVariable String id) {
        return eventRepo.findById(id);
    }

    @PutMapping("/index")
    public Mono<Void> index(@RequestBody Event evt) {
        return eventRepo.index(evt);
    }

    @PutMapping("/indexAll")
    public Mono<Void> indexAll(@RequestBody List<Event> events) {
        return eventRepo.indexAll(events);
    }

    @PostMapping("/search")
    public Mono<Page<Event>> search(@RequestBody EventSearchParams params) {
        return eventRepo.search(params);
    }
}
