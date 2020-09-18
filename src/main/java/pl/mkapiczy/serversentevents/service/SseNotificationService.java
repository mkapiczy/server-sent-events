package pl.mkapiczy.serversentevents.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pl.mkapiczy.serversentevents.mapper.EventMapper;
import pl.mkapiczy.serversentevents.model.EventDto;
import pl.mkapiczy.serversentevents.repository.EmitterRepository;

import java.io.IOException;

@Service
@Primary
@AllArgsConstructor
@Slf4j
public class SseNotificationService implements NotificationService {

    private final EmitterRepository emitterRepository;
    private final EventMapper eventMapper;

    @Override
    public void sendNotification(String memberId, EventDto event) {
        if (event == null) {
            log.debug("No server event to send to device.");
            return;
        }
        doSendNotification(memberId, event);
    }

    private void doSendNotification(String memberId, EventDto event) {
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                log.debug("Sending event: {} for member: {}", event, memberId);
                sseEmitter.send(eventMapper.toSseEventBuilder(event));
            } catch (IOException | IllegalStateException e) {
                log.debug("Error while sending event: {} for member: {} - exception: {}", event, memberId, e);
                emitterRepository.remove(memberId);
            }
        }, () -> log.debug("No emitter for member {}", memberId));
    }

}
