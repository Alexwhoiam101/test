package com.trytocopyit.controller;

import com.trytocopyit.entity.Acc;
import com.trytocopyit.entity.Game;
import com.trytocopyit.form.GameForm;
import com.trytocopyit.form.UserForm;
import com.trytocopyit.repository.UserRepository;
import com.trytocopyit.service.UserService;
import com.trytocopyit.validator.UserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import org.apache.commons.lang.exception.ExceptionUtils;
import com.trytocopyit.repository.OrderRepository;
import com.trytocopyit.repository.GameRepository;
import com.trytocopyit.model.OrderDetailInfo;
import com.trytocopyit.model.OrderInfo;
import com.trytocopyit.pagination.PaginationResult;
import com.trytocopyit.validator.GameFormValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

@Controller
@Transactional
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameFormValidator gameFormValidator;

    @Autowired
    private UserFormValidator userFormValidator;

    @Autowired
    private UserService service;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == GameForm.class) {
            dataBinder.setValidator(gameFormValidator);
        }

        if(target.getClass() == UserForm.class){
            dataBinder.setValidator(userFormValidator);
        }
    }

    
    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model, String message) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("message");
        return "login";
    }

    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.POST)
    public String loginWork(Model model,
                        @ModelAttribute("userForm") @Validated UserForm userForm,
                            BindingResult result,
                            final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            return "login";
        }
        try {
            Acc user = userRepository.findAccount(userForm.getUsername());
            if (user != null) {
                if(!user.isActive()){
                    model.addAttribute("message", "The user is blocked, contact the moderator");
                    return "login";
                }
                if (!bCryptPasswordEncoder.matches(userForm.getPassword(), user.getEncrytedPassword())  ) {
                    service.increaseFailedAttempts(user);
                    model.addAttribute("message", String.format("Error password, try %d", user.getFailedAttempt()));
                    return "login";
                } else {
                    service.resetFailedAttempts(user.getUserName());
                    service.autoLogin(userForm.getUsername(), bCryptPasswordEncoder.encode(userForm.getPassword()));
                }
            } else {
                model.addAttribute("message", String.format("User %s, not found", userForm.getUsername()));
                return "login";
            }
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("message", message);
            return "login";
        }

        return "redirect:/admin/accountInfo";
    }

    @RequestMapping(value = { "/admin/logout" }, method = RequestMethod.GET)
    public String logoutSec(){
        service.logout();
        return "redirect:/admin/login";
    }


    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());

        model.addAttribute("userDetails", userDetails);
        return "accountInfo";
    }

    @RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;

        PaginationResult<OrderInfo> paginationResult = orderRepository.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }


    @RequestMapping(value = { "/admin/game" }, method = RequestMethod.GET)
    public String game(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        GameForm gameForm = null;

        if (code != null && code.length() > 0) {
            Game game = gameRepository.findGame(code);
            if (game != null) {
                gameForm = new GameForm(game);
            }
        }
        if (gameForm == null) {
            gameForm = new GameForm();
            gameForm.setNewGame(true);
        }
        model.addAttribute("gameForm", gameForm);
        return "game";
    }


    @RequestMapping(value = { "/admin/game" }, method = RequestMethod.POST)
    public String gameSave(Model model,
                              @ModelAttribute("gameForm") @Validated GameForm gameForm,
                              BindingResult result,
                              final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()){
            return "game";
        }
        try {
            gameRepository.save(gameForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            return "game";
        }

        return "redirect:/gameList";
    }

    @RequestMapping(value = { "/admin/game/delete" }, method = RequestMethod.POST)
    public String gameDelete(Model model,
                           @ModelAttribute("gameForm") GameForm gameForm,
                           BindingResult result,
                           final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "game";
        }
        try {
            gameRepository.delete(gameForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            return "game";
        }

        return "redirect:/gameList";
    }

    @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderRepository.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        List<OrderDetailInfo> details = this.orderRepository.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);

        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }
}
