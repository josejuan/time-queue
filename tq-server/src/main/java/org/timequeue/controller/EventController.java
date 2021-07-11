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
import org.timequeue.pojo.EventForm;
import org.timequeue.pojo.Notification;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.timequeue.controller.ControllerUtil.getUser;

@Controller
@RequestMapping("/p/event")
public class EventController {

    @Autowired
    private Events events;

    @Autowired
    private EntityManager entityManager;

    @GetMapping({"", "/", "/{id}"})
    public String get(@PathVariable(name = "id", required = false) UUID _id, Model model) {
        final UUID id = _id == null ? UUID.randomUUID() : _id;

        final Event event = getUser().getEvents().stream().filter(x -> id.equals(x.getId())).findAny().orElseGet(() -> makeEvent(id));
        // some datetime-local input controls require do not set seconds nor milis
        event.setNextEvent(event.getNextEvent().truncatedTo(ChronoUnit.MINUTES));

        final List<Notification> notifications = event.getMinutesAfterNotifications().stream().map(Notification::from).collect(toList());

        final EventForm form = new EventForm(event, notifications);

        model.addAttribute("form", form);
        model.addAttribute("updateModes", UpdateMode.values());

        return "event";
    }

    @PostMapping(value = {"", "/"}, params = "save")
    public String postSave(@ModelAttribute EventForm form, Model model) {

        form.getEvent().setUser(getUser());
        form.getEvent().setMinutesAfterNotifications(
                ofNullable(form.getNotifications())
                        .orElse(emptyList())
                        .stream()
                        .filter(x -> x.getKey() != null)
                        .map(Notification::getMinutes)
                        .collect(toSet()));

        events.save(form.getEvent());

        return "redirect:/p/event/" + form.getEvent().getId().toString();
    }

    @PostMapping(value = {"", "/"}, params = "cancel")
    public String postCancel(@ModelAttribute EventForm form, Model model) {
        return "redirect:/p/event/" + form.getEvent().getId().toString();
    }

    @PostMapping(value = {"", "/"}, params = "delete")
    public String postDelete(@ModelAttribute EventForm form, Model model) {
        events.findById(form.getEvent().getId()).ifPresent(e -> {
            e.getUser().getEvents().remove(e);
            events.delete(e);
        });
        return "redirect:/p/events";
    }

    private Event makeEvent(UUID id) {
        final Event e = new Event();
        e.setId(id);
        e.setTitle(null);
        e.setDescription(null);
        e.setNextEvent(LocalDateTime.now());
        e.setLastNotification(null);
//        e.setMinutesAfterNotifications(emptySet());
        e.setUpdateMode(UpdateMode.NEVER);
        return e;
    }

}