package com.weather;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/authLogin")
    public ModelAndView showAuthLogin() {
        ModelAndView modelAndView = new ModelAndView("authLogin");
        return modelAndView;
    }
}
