package pl.mkapiczy.serversentevents.service;

import pl.mkapiczy.serversentevents.model.EventDto;

public interface NotificationService {

    void sendNotification(String memberId, EventDto event);
}
