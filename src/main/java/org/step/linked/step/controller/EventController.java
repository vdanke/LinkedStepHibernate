package org.step.linked.step.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.step.linked.step.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/send")
    public ResponseEntity<Object> sendEvent(@RequestBody String event) {
        eventService.sendEventToEventLogger(event);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/send/file-event")
    public ResponseEntity<Object> sendFileEvent() {
        eventService.sendFileEventToLoggerService();
        return ResponseEntity.ok().build();
    }
}
