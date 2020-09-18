package pl.mkapiczy.serversentevents.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.mkapiczy.serversentevents.model.EventDto;
import pl.mkapiczy.serversentevents.service.EmitterService;
import pl.mkapiczy.serversentevents.service.NotificationService;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    public static final String MEMBER_ID_HEADER = "MemberId";

    private final EmitterService emitterService;
    private final NotificationService notificationService;

    @GetMapping
    public SseEmitter subscribeToEvents(@RequestHeader(name = MEMBER_ID_HEADER) String memberId) {
        log.debug("Subscribing member with id {}", memberId);
        return emitterService.createEmitter(memberId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publishEvent(@RequestHeader(name = MEMBER_ID_HEADER) String memberId, @RequestBody EventDto event) {
        log.debug("Publishing event {} for member with id {}", event, memberId);
        notificationService.sendNotification(memberId, event);
    }
}
