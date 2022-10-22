package com.learnwords.webapp.controllers.usercontrollers;

import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService service;

    @GetMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "authorization/registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("user") User user, Model model) { //TODO: сделать валидацию юзера
        if (!user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordNotEquals", "Пароли не совпадают");
            return "authorization/registration";
        }

        if (!service.saveUser(user)) {
            model.addAttribute("notUniqueUser", "Пользователь с таким ником уже существует");
            return "authorization/registration";
        }

        return "redirect:/login";
    }
}
