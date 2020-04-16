package com.muttsapp.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

   
    @RequestMapping(value="/")
    public String home(){

        return "home";
    }
   
    @RequestMapping(value="/user")
    public String user(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("User has authorities: " + userDetails.getAuthorities());

        return "user";
    }
  
    @RequestMapping(value="/admin")
    public String admin(Model model){

        return "admin";

    }
   
    @RequestMapping(value="/login")
    public String login(){

        return "login";
    }
   
    @RequestMapping(value="/403")
    public String Error403(){

        return "403";
    }
}
