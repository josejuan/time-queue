package org.timequeue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.timequeue.auth.PBKDF2;
import org.timequeue.data.repo.Users;
import org.timequeue.pojo.ChangePassword;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.timequeue.controller.ControllerUtil.error;
import static org.timequeue.controller.ControllerUtil.getUser;
import static org.timequeue.controller.ControllerUtil.info;

@Controller
@RequestMapping("/p/changepassword")
public class ChangePasswordController {

    @Autowired
    private Users users;

    @GetMapping
    public String get(Model model) {
        model.addAttribute("changePassword", new ChangePassword());
        return "changepassword";
    }

    @PostMapping
    public String post(@ModelAttribute ChangePassword changePassword, Model model) {
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
        return get(model);
    }

}