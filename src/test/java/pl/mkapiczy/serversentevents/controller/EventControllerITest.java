package pl.mkapiczy.serversentevents.controller;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.mkapiczy.serversentevents.model.EventDto;
import pl.mkapiczy.serversentevents.utils.AsyncEventClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.mkapiczy.serversentevents.controller.EventController.MEMBER_ID_HEADER;
import static pl.mkapiczy.serversentevents.utils.TestConstants.EVENT_DTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerITest {

    private AsyncEventClient asyncEventClient;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int serverPort;

    private String memberId;

    @BeforeEach
    void setup() {
        memberId = UUID.randomUUID().toString();
        this.asyncEventClient = new AsyncEventClient(serverPort);
    }

    @Test
    void shouldReceiveEvent() throws InterruptedException {
        var eventStream = asyncEventClient.getEvents(memberId);

        List<EventDto> receivedEvents = new ArrayList<>();
        var disposable = eventStream.subscribe(
                event -> {
                    receivedEvents.add(new EventDto(event.event(), event.data()));
                },
                error -> {throw new AssertionError("Event stream error");});

        waitForSubscription();
        publishEvent(memberId);

        Awaitility.await()
                .pollDelay(3, TimeUnit.SECONDS)
                .until(() -> receivedEvents.size() == 1);
        EventDto receivedEvent = receivedEvents.get(0);
        assertThat(receivedEvent).isEqualToComparingFieldByField(EVENT_DTO);
        disposable.dispose();

    }

    @Test
    void shouldNotReceiveEventForWrongMemberId() throws InterruptedException {
        var eventStream = asyncEventClient.getEvents(memberId);

        List<EventDto> receivedEvents = new ArrayList<>();
        var disposable = eventStream.subscribe(
                event -> receivedEvents.add(new EventDto(event.event(), event.data())),
                error -> {throw new AssertionError("Event stream error");});

        waitForSubscription();
        publishEvent("WRONG_MEMBER_ID");

        Awaitility.await()
                .pollDelay(3, TimeUnit.SECONDS)
                .until(receivedEvents::isEmpty);
        disposable.dispose();
    }

    @Test
    void shouldGetBadRequestOnSubscriptionWithoutMemberId() {
        var statusCode = asyncEventClient.getEventsWithoutHeader();
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private void publishEvent(String memberId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(MEMBER_ID_HEADER, memberId);
        headers.add("Content-Type", "application/json");
        testRestTemplate.exchange("/events", HttpMethod.POST, new HttpEntity<>(EVENT_DTO, headers), Void.class);
    }

    private void waitForSubscription() throws InterruptedException {
        Thread.sleep(200);
    }
}