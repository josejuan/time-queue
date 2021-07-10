package org.timequeue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.timequeue.controller.ControllerUtil.getUser;

@Controller
@RequestMapping("/p/profile")
public class ProfileController {

    @GetMapping
    public String get(Model model) {
        model.addAttribute("user", getUser());
        return "profile";
    }

}