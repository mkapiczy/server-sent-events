package pl.mkapiczy.serversentevents.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

import static pl.mkapiczy.serversentevents.controller.EventController.MEMBER_ID_HEADER;

@Slf4j
public class AsyncEventClient {

    private WebClient webClient;

    private final ParameterizedTypeReference<ServerSentEvent<Map<String, Object>>> type =
            new ParameterizedTypeReference<>() {
            };

    public AsyncEventClient(int serverPort) {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + serverPort)
                .build();
    }

    public Flux<ServerSentEvent<Map<String, Object>>> getEvents(String memberId) {
        var requestSpec = webClient.get()
                .uri("/events")
                .header(MEMBER_ID_HEADER, memberId);

        return requestSpec.retrieve().bodyToFlux(type);
    }

    public HttpStatus getEventsWithoutHeader() {
        Mono<ClientResponse> response = webClient.get()
                .uri("/events")
                .exchange();

        return response.block(Duration.ofMillis(500)).statusCode();
    }

}
