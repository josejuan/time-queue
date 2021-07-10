package org.timequeue.controller;

import org.apache.logging.log4j.Level;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.timequeue.data.model.User;
import org.timequeue.pojo.Msg;

import java.util.ArrayList;
import java.util.List;

public class ControllerUtil {

    public static List<Msg> msg(Model model) {
        if (!model.containsAttribute("messages"))
            model.addAttribute("messages", new ArrayList<Msg>());
        return (List<Msg>) model.getAttribute("messages");
    }

    public static void msg(Model model, Level level, String format, Object... args) {
        msg(model).add(new Msg(level, String.format(format, args)));
    }

    public static void error(Model model, String format, Object... args) {
        msg(model).add(new Msg(Level.ERROR, String.format(format, args)));
    }

    public static void warn(Model model, String format, Object... args) {
        msg(model).add(new Msg(Level.WARN, String.format(format, args)));

    }

    public static void info(Model model, String format, Object... args) {
        msg(model).add(new Msg(Level.INFO, String.format(format, args)));
    }

    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
