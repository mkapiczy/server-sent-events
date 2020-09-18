package pl.mkapiczy.serversentevents.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.mkapiczy.serversentevents.utils.TestConstants.EVENT_DTO;

class EventMapperTest {

    private EventMapper eventMapper = new EventMapper();

    @Test
    void shouldMapToSseEventBuilder() {
        SseEmitter.SseEventBuilder sseEventBuilder = eventMapper.toSseEventBuilder(EVENT_DTO);

        assertThat(sseEventBuilder).isNotNull();
    }

}