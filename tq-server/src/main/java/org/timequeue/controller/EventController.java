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
@RequestMapping("/p/event")
public class EventController {

    @Autowired
    private Events events;

    @GetMapping({"", "/", "/{id}"})
    public String get(@PathVariable(name = "id", required = false) UUID _id, Model model) {
        final UUID id = _id == null ? UUID.randomUUID() : _id;
        model.addAttribute("event", getUser().getEvents().stream().filter(e -> id.equals(e.getId())).findAny().orElseGet(() -> makeEvent(id)));
        return "event";
    }

    @PostMapping({"", "/"})
    public String post(@ModelAttribute Event e, Model model) {

        e.setUser(getUser());

        e.setNextEvent(OffsetDateTime.now());
        e.setMinutesAfterNotifications(emptySet());
        e.setUpdateMode(UpdateMode.NEVER);

        events.save(e);
        return "redirect:/p/event/" + e.getId().toString();
    }

    private Event makeEvent(UUID id) {
        final Event e = new Event();
        e.setId(id);
        e.setTitle(null);
        e.setDescription(null);
        e.setNextEvent(null);
        e.setLastNotification(null);
        e.setMinutesAfterNotifications(emptySet());
        e.setUpdateMode(UpdateMode.NEVER);
        return e;
    }

}
