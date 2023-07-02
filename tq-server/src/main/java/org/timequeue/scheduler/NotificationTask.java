package org.timequeue.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.timequeue.data.repo.Events;
import org.timequeue.service.EventNotifier;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.StreamSupport;

@Component
public class NotificationTask {

    @Autowired
    private Events events;

    @Autowired
    private EventNotifier eventNotifier;

    @Scheduled(initialDelay = 30_000, fixedRate = 30_000)
    public void notifyPendingEvents() {
        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        StreamSupport
                .stream(events.findAll().spliterator(), false)
                .filter(e -> e.getNextNotification() != null && e.getNextNotification().isBefore(now))
                .forEach(eventNotifier::notifyEvent);
    }

}
