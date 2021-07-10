package org.timequeue.controller;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.timequeue.auth.PBKDF2;
import org.timequeue.data.model.User;
import org.timequeue.data.repo.Users;
import org.timequeue.pojo.ChangePassword;
import org.timequeue.pojo.Msg;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/p")
public class UserProfileController {

    @Autowired
    private Users users;

    private static List<Msg> msg(Model model) {
        if (!model.containsAttribute("messages"))
            model.addAttribute("messages", new ArrayList<Msg>());
        return (List<Msg>) model.getAttribute("messages");
    }

    private static void msg(Model model, Level level, String format, Object... args) {
        msg(model).add(new Msg(level, String.format(format, args)));
    }

    private static void error(Model model, String format, Object... args) {
        msg(model).add(new Msg(Level.ERROR, String.format(format, args)));
    }

    private static void warn(Model model, String format, Object... args) {
        msg(model).add(new Msg(Level.WARN, String.format(format, args)));

    }

    private static void info(Model model, String format, Object... args) {
        msg(model).add(new Msg(Level.INFO, String.format(format, args)));
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("user", getUser());
        model.addAttribute("changePassword", new ChangePassword());
        return "userprofile";
    }

    @PostMapping("/changepassword")
    public String changepassword(@ModelAttribute ChangePassword changePassword, Model model) {
        if (changePassword.getNew1() == null || changePassword.getNew1().trim().length() < 1)
            error(model, "new password cannot be empty!");
        else if (!changePassword.getNew1().equals(changePassword.getNew2()))
            error(model, "new password 1 does not match with password 2!");
        else if (!PBKDF2.validatePassword(changePassword.getOld(), getUser().getPwd()))
            error(model, "wrong current (old) password!");
        else {
            try {
                getUser().setPwd(PBKDF2.generateStorngPasswordHash(changePassword.getNew1()));
                users.save(getUser());
                info(model, "password changed!");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                error(model, e.getLocalizedMessage());
            }
        }
        return main(model);
    }

}