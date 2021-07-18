package org.timequeue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.timequeue.data.model.Event;
import org.timequeue.data.repo.Events;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.timequeue.app.AppConfiguration.USER_ZONE_ID;

@Service
public class EventStore {

    @Autowired
    private Events events;

    public void save(Event e) {

        // compute the next notification
        final LocalDateTime eventDate = e.getNextEvent().atZone(USER_ZONE_ID).toOffsetDateTime().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        Optional<LocalDateTime> nextEvent = e.getMinutesAfterNotifications().stream()
                .map(m -> eventDate.minus(m, ChronoUnit.MINUTES))
                .filter(f -> f.isAfter(LocalDateTime.now(ZoneOffset.UTC)))
                .min(LocalDateTime::compareTo);

        e.setNextNotification(nextEvent.orElse(null));

        events.save(e);
    }
}
