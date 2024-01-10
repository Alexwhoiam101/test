package com.trytocopyit.controller;

import com.trytocopyit.entity.Acc;
import com.trytocopyit.form.GameForm;
import com.trytocopyit.form.UserForm;
import com.trytocopyit.repository.UserRepository;
import com.trytocopyit.service.UserService;
import com.trytocopyit.validator.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.trytocopyit.service.CaptchaService;
import cn.apiclub.captcha.Captcha;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService service;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String registerUser(Model model) {
        Acc user = new Acc();
        getCaptcha(user);
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/save")
    public String saveUserStage2(Model model) {
        return "redirect:/admin/login";
    }

    @PostMapping("/save")
    public String saveUser(
            @ModelAttribute Acc user,
            Model model
    ) {
        String username = user.getUserName();
        if (username != null && username.length() > 0) {
            if (username.matches("\\s+") || !username.matches("[a-zA-Z]+")) {
                model.addAttribute("message","Username pattern is not valid");
                getCaptcha(user);
                model.addAttribute("user", user);
                return "register";
            }else if(userRepository.findAccount(username) != null){
                model.addAttribute("message","Username pattern is not valid");
                getCaptcha(user);
                model.addAttribute("user", user);
                return "register";
            }
        }else{
            model.addAttribute("message","Username is required");
            getCaptcha(user);
            model.addAttribute("user", user);
            return "register";
        }
        if(user.getCaptcha().equals(user.getHiddenCaptcha())) {
            user.setEncrytedPassword(bCryptPasswordEncoder.encode(user.getEncrytedPassword()));
            service.save(user);
            model.addAttribute("message", "User Registered successfully!");
            return "redirect:/admin/login";
        }
        else {
            model.addAttribute("message", "Invalid Captcha");
            getCaptcha(user);
            model.addAttribute("user", user);
        }
        return "register";
    }

    private void getCaptcha(Acc user) {
        Captcha captcha = CaptchaService.createCaptcha(240, 70);
        user.setHiddenCaptcha(captcha.getAnswer());
        user.setCaptcha("");
        user.setRealCaptcha(CaptchaService.encodeCaptcha(captcha));

    }
}
