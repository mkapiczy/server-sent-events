package pl.mkapiczy.serversentevents.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.mkapiczy.serversentevents.model.EventDto;

import java.util.Map;

public class TestConstants {
    public static final String MEMBER_ID = "MEMBER_ID";
    public static final String TYPE = "type";
    public static final Map<String, Object> BODY = Map.of("Result", "SUCCESS");
    public static final EventDto EVENT_DTO = new EventDto(TYPE, BODY);

    public static final SseEmitter.SseEventBuilder SSE_EVENT_BUILDER = SseEmitter.event()
            .id(RandomStringUtils.randomAlphanumeric(12))
            .name(TYPE)
            .data(BODY);
}
