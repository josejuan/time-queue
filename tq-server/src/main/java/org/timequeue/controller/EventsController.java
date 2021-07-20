package org.timequeue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.timequeue.data.model.Event;
import org.timequeue.data.repo.Events;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;
import static org.timequeue.controller.ControllerUtil.getUser;

@Controller
@RequestMapping("/p/events")
public class EventsController {

    @Autowired
    private Events events;

    static Comparator<Event> by(Function<Event, LocalDateTime> x) {
        return comparing(x, nullsLast(naturalOrder()));
    }

    @GetMapping
    public String get(Model model) {
        model.addAttribute("events", getUser().getEvents().stream()
                .sorted(by(Event::getNextNotification).thenComparing(by(Event::getNextEvent)))
                .collect(toList()));
        return "events";
    }
}
