package org.timequeue.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.timequeue.data.model.Event;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EventForm {
    private Event event;
    private List<Notification> notifications;
}
