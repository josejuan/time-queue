package org.timequeue.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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

    @Value("${timeq.deployment}")
    private String deployment;

    @Value("${timeq.mail.subject}")
    private String fromSubject;

    @Value("${timeq.mail.from}")
    private String fromMail;

    @Value("${timeq.telegram.key}")
    private String telegramKey;

    @Value("${timeq.telegram.chat}")
    private String telegramChat;

    public void notifyEvent(Event event) {

        final boolean sent = sendEmail(event) || sendTelegram(event);

        if(!sent) {
            System.out.printf("ERROR: cannot notify message '%s'%n", event.getTitle());
            return;
        }

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

    private boolean sendTelegram(Event event) {
        try {
            String msg = fromSubject + event.getTitle() + "\n\n" + formatBody(event);
            String url = "https://api.telegram.org/bot" + telegramKey + "/sendMessage";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>("chat_id=" + telegramChat + "&text=" + msg, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode ok = objectMapper.readTree(response.getBody()).get("ok");
            if (!(ok instanceof BooleanNode) || !ok.asBoolean())
                System.out.printf("ERROR: cannot send Telegram to chat %s (responde was false)%n", telegramChat);
            else
                System.out.printf("Sent message to chat %s%n", telegramChat);
            return true;
        } catch (Throwable e) {
            System.err.printf("ERROR: cannot send Telegram, error: %s", e.getLocalizedMessage());
            return false;
        }
    }

    private boolean sendEmail(Event event) {
        try {
            final SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(event.getUser().getEmail());
            msg.setFrom(fromMail);
            msg.setSubject(fromSubject + event.getTitle());
            msg.setText(formatBody(event));
            mailSender.send(msg);
            System.out.printf("Sent mail to %s%n", event.getUser().getEmail());
            return true;
        } catch (Throwable e) {
            System.err.printf("ERROR: cannot send email, error: %s", e.getLocalizedMessage());
            return false;
        }
    }

    private String formatBody(Event event) {
        return "" +
                event.getTitle() + "\n" +
                "\n" +
                event.getDescription() + "\n" +
                "\n" +
                "https://" + deployment + "-tq.wikicurriculum.org/p/event/" + event.getId();
    }
}
