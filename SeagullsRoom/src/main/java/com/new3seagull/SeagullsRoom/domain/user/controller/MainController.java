package com.new3seagull.SeagullsRoom.domain.user.controller;

import com.new3seagull.SeagullsRoom.global.util.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MainController {
    @GetMapping("/")
    public ResponseEntity<?> mainP(Principal principal) {
        System.out.println(ApiUtils.success(principal.getName()));
        return ResponseEntity.ok(ApiUtils.success(principal.getName()));
    }
}