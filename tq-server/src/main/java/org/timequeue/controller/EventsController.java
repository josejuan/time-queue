package org.timequeue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.timequeue.data.model.Event;
import org.timequeue.data.model.UpdateMode;
import org.timequeue.data.repo.Events;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.Collections.emptySet;
import static org.timequeue.controller.ControllerUtil.getUser;

@Controller
@RequestMapping("/p/events")
public class EventsController {

    @Autowired
    private Events events;

    @GetMapping
    public String get(Model model) {
        model.addAttribute("events", getUser().getEvents());
        return "events";
    }
}
