package com.stocknest.stocknest_api.controller;

import com.stocknest.stocknest_api.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    @Autowired
    private final MailService mailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String body) {
        mailService.sendEmail(to, subject, body);
        return "Email sent successfully!";
    }
}
