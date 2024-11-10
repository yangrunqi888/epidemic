package com.yrq.controller;

import com.yrq.service.impl.MessageSender;
import com.yrq.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rubbish")
public class RubbishController {

    private final MessageSender messageSender;

    @Autowired
    public RubbishController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @PostMapping("/send")
    public R sendToQueue(@RequestBody String message) {
        messageSender.sendMessage(message);
        return R.ok("Message sent to queue.");
    }
}
