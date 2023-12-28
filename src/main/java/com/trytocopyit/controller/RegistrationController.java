package com.trytocopyit.controller;

import java.util.List;

import com.trytocopyit.entity.Acc;
import com.trytocopyit.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.trytocopyit.service.CaptchaService;
import cn.apiclub.captcha.Captcha;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserServiceInterface service;

    @GetMapping("/")
    public String registerUser(Model model) {
        Acc user = new Acc();
        getCaptcha(user);
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/save")
    public String saveUser(
            @ModelAttribute Acc user,
            Model model
    ) {
        if(user.getCaptcha().equals(user.getHiddenCaptcha())) {
            service.save(user);
            model.addAttribute("message", "User Registered successfully!");
            return "redirect:login";
        }
        else {
            model.addAttribute("message", "Invalid Captcha");
            getCaptcha(user);
            model.addAttribute("user", user);
        }
        return "register";
    }

    @GetMapping("/allUsers")
    public String getAllUsers(Model model) {
        List<Acc> userList= service.getAllUsers();
        model.addAttribute("userList", userList);
        return "";//html templates for all users
    }

    private void getCaptcha(Acc user) {
        Captcha captcha = CaptchaService.createCaptcha(240, 70);
        user.setHiddenCaptcha(captcha.getAnswer());
        user.setCaptcha("");
        user.setRealCaptcha(CaptchaService.encodeCaptcha(captcha));

    }
}
