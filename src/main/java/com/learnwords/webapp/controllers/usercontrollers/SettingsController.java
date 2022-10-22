package com.learnwords.webapp.controllers.usercontrollers;

import com.learnwords.webapp.models.User;
import com.learnwords.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Value("${uploads.path}")
    private String path;

    @Autowired
    private UserService service;

    @GetMapping
    public String showSettings(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", service.userByUsername(user.getUsername()));
        return "authorization/settings";
    }

    @PutMapping
    public String updateSettings(@AuthenticationPrincipal User currentUser,
                                 @ModelAttribute User user, Model model,
                                 @RequestParam MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdir();
            //рандомный идентификатор + имя файла
            String resultFilename = UUID.randomUUID() + file.getOriginalFilename();

            //сохранение файла в директорию
            file.transferTo(new File(path + '/' + resultFilename));

            user.setAvatar(resultFilename);
        }

        if (!user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordNotEquals", "Пароли не совпадают");
            return "authorization/settings";
        }

        if (!service.saveUser(currentUser, user)) {
            model.addAttribute("notUniqueUser", "Пользователь с таким ником уже существует");
            return "authorization/settings";
        }
        return "redirect:/settings";
    }
}
