package pl.mkapiczy.serversentevents.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mkapiczy.serversentevents.repository.EmitterRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmitterServiceTest {

    @Mock
    private EmitterRepository emitterRepository;

    private EmitterService emitterService;

    @BeforeEach
    void setUp() {
        emitterService = new EmitterService(10, emitterRepository);
    }

    @Test
    void shouldReturnNewEmitter() {
        var memberId = UUID.randomUUID().toString();

        var emitter = emitterService.createEmitter(memberId);

        assertThat(emitter).isNotNull();
        verify(emitterRepository).addOrReplaceEmitter(eq(memberId), eq(emitter));
    }

}