package com.kcc.trioffice.domain.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TokenController {

    @GetMapping("/token")
    public String getToken() {
        return "component/token-save";
    }
}
