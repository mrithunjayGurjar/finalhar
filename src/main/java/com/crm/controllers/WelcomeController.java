
package com.crm.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    
    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CRM Ticketing System API");
        response.put("status", "running");
        response.put("endpoints", Map.of(
            "register", "POST /auth/register",
            "login", "POST /auth/login",
            "tickets", "GET/POST /api/tickets",
            "documentation", "See README.md for full API documentation"
        ));
        return response;
    }
}
