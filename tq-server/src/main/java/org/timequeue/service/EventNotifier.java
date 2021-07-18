package org.timequeue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.timequeue.data.model.Event;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.timequeue.data.model.UpdateMode.NEVER;

@Service
public class EventNotifier {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EventStore eventStore;

    @Value("${timeq.mail.from}")
    private String fromMail;

    public void notifyEvent(Event event) {

        final SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(event.getUser().getEmail());
        msg.setFrom(fromMail);
        msg.setSubject("Time Queue! " + event.getTitle());
        msg.setText("Time Queue remember you:\n\n" + event.getDescription());
        mailSender.send(msg);
        System.out.printf("Sent mail to %s%n", event.getUser().getEmail());

        event.setLastNotification(LocalDateTime.now(ZoneOffset.UTC));
        eventStore.save(event);
        if (event.getNextNotification() == null && !NEVER.equals(event.getUpdateMode())) {
            switch (event.getUpdateMode()) {
                case DAILY:
                    event.setNextEvent(event.getNextEvent().plusDays(1));
                    break;
                case WEEK:
                    event.setNextEvent(event.getNextEvent().plusWeeks(1));
                    break;
                case MONTH:
                    event.setNextEvent(event.getNextEvent().plusMonths(1));
                    break;
                case ANNUALLY:
                    event.setNextEvent(event.getNextEvent().plusYears(1));
                    break;
                default:
                    throw new RuntimeException(event.getUpdateMode().toString() + " is not implemented!");
            }
            eventStore.save(event);
        }
    }
}
