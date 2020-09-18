package pl.mkapiczy.serversentevents.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.mkapiczy.serversentevents.repository.EmitterRepository;

@Service
@Slf4j
public class EmitterService {

    private final long eventsTimeout;
    private final EmitterRepository repository;

    public EmitterService(@Value("${events.connection.timeout}") long eventsTimeout,
                          EmitterRepository repository) {
        this.eventsTimeout = eventsTimeout;
        this.repository = repository;
    }

    public SseEmitter createEmitter(String memberId) {
        SseEmitter emitter = new SseEmitter(eventsTimeout);
        emitter.onCompletion(() -> repository.remove(memberId));
        emitter.onTimeout(() -> repository.remove(memberId));
        emitter.onError(e -> {
            log.error("Create SseEmitter exception", e);
            repository.remove(memberId);
        });
        repository.addOrReplaceEmitter(memberId, emitter);
        return emitter;
    }

}
