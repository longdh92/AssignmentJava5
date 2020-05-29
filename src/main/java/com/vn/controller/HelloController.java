package com.vn.controller;

import com.vn.model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
    @RequestMapping(value = "/trang-chu")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("title", "Home Page");
        modelAndView.addObject("message", "Hello World!");
        modelAndView.addObject("account", new Account("longdh", "123456", "longdh@gmail.com"));
        return modelAndView;
    }
}
