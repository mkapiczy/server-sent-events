package pl.mkapiczy.serversentevents.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.mkapiczy.serversentevents.mapper.EventMapper;
import pl.mkapiczy.serversentevents.repository.EmitterRepository;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static pl.mkapiczy.serversentevents.utils.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class SseNotificationServiceTest {
    @Mock
    private EmitterRepository emitterRepository;
    @Mock
    private EventMapper eventMapper;
    @InjectMocks
    private SseNotificationService sseNotificationService;

    @Test
    void shouldSendEvent() throws IOException {
        SseEmitter emitterSpy = spy(new SseEmitter());
        when(emitterRepository.get(MEMBER_ID)).thenReturn(Optional.of(emitterSpy));
        when(eventMapper.toSseEventBuilder(EVENT_DTO)).thenReturn(SSE_EVENT_BUILDER);

        sseNotificationService.sendNotification(MEMBER_ID, EVENT_DTO);

        verify(emitterSpy).send(eq(SSE_EVENT_BUILDER));
    }

    @Test
    void shouldDoNothingOnPassedNullEvent() {
        sseNotificationService.sendNotification(MEMBER_ID, null);

        verifyNoInteractions(emitterRepository);
        verifyNoInteractions(eventMapper);
    }

    @Test
    void shouldDoNothingOnMissingEmitter() {
        when(emitterRepository.get(MEMBER_ID)).thenReturn(Optional.empty());

        sseNotificationService.sendNotification(MEMBER_ID, EVENT_DTO);

        verifyNoInteractions(eventMapper);
    }

}