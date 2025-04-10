package org.dexteradei.whitegold.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth/callback")
public class OAuth2CallbackController {
    
    @GetMapping("/kite")
    public String handleRedirection(@RequestParam("request_token") String requestToken) {
        System.out.println("✅ GOT REQUEST TOKEN");
        System.out.println("------------------\n\n" + requestToken + "\n\n------------------");
        return "GodFather";
    } 
}
