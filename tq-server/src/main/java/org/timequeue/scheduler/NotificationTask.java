package org.timequeue.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.timequeue.data.repo.Events;
import org.timequeue.service.EventNotifier;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class NotificationTask {

    @Autowired
    private Events events;

    @Autowired
    private EventNotifier eventNotifier;

    @Scheduled(initialDelay = 1_000, fixedRate = 5_000)
    public void notifyPendingEvents() {
        events.findPendingNotifications(LocalDateTime.now(ZoneOffset.UTC)).forEach(eventNotifier::notifyEvent);
    }

}
