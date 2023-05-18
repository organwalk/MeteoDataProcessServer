package com.weather;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/authLogin")
    public ModelAndView showAuthLogin(@RequestParam(name = "error", required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("authLogin");
        if (error != null) {
            modelAndView.addObject("errorMessage", "用户名或密码错误");
        }
        return modelAndView;
    }
}
