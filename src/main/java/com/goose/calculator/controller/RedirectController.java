package com.goose.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class RedirectController{

    @GetMapping({"swagger-ui", "swagger-ui/", "/"})
    public RedirectView redirectToHomepage() {
        return new RedirectView("/swagger-ui/index.html", true);
    }
}