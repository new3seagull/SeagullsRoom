package com.new3seagull.SeagullsRoom.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@ResponseBody
public class MainController {
    @GetMapping("/")
    public String mainP(Principal principal) {
        String name1 = principal.getName();


        return "main Controller" + name1;
    }
}