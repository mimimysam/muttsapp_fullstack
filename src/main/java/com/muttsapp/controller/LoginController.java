package com.muttsapp.controller;

import com.muttsapp.tables.User;
import com.muttsapp.services.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
     private UserLoginService userLoginService;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userLoginService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userLoginService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }
    @RequestMapping(value="/index", method = RequestMethod.GET)
    public String index(Authentication auth, Model model){
        System.out.println(auth.getName());
        int user_id = userLoginService.findUserByEmail(auth.getName()).getId();
        model.addAttribute("user_id", user_id);
        return "index";
    }

}