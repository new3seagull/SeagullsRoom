package com.new3seagull.SeagullsRoom.domain.user.controller;

import com.new3seagull.SeagullsRoom.domain.user.dto.JoinDto;
import com.new3seagull.SeagullsRoom.domain.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/api/v1/users")
    public String joinProcess(@RequestBody JoinDto joinDto) {
        joinService.joinProcess(joinDto);
        return "ok";
    }
}
