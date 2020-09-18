# Server Sent Events
Service for sending real time events to the client 

### Server sent event
Service is using Server Sent Event for one-way communication with client.
[SSE mozilla web doc refrence]('https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events)

Events are returned as `text/event-stream` divided into 
* `data:` returns json string with body object, includes ISO-8601 timestamp
* `event:` returns type of event
* `id:` id of event used to keep track of which event was received by client


## API

### Endpoints

| Path      | Method     | Description                                       | Headers     | Body   | 
| :-------: |:----------:| :------------------------------------------------:| :----------:| :-----:| 
| `/events` | `GET`      | Subscribe for events for given member             | `Member-ID`, `Device-Id`, `Last-Event-ID` ||
| `/events` | `POST`     | Publish `Event` to given member                   | `Member-ID` | `Event`|

### Event object
Structure:
```json
{
    "event": {
        "type": "EVENT_TYPE_WITH_UPPER_CASE",
        "body": {
            "examplePropertyOfEvent": "exampleValueOfProperty"
        }
    }
}
```

### Testing
You can use included postman collection for publishing the events. However, postman does not support event-stream content type, so for receiving the events you need to use another client, for instace:
```
 curl -i -H "MemberId: 123" http://localhost:8080/notification/events
```