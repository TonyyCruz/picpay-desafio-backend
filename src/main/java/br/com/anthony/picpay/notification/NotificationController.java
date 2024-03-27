package br.com.anthony.picpay.notification;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/notification")
public class NotificationController {

    @GetMapping
    public Map<String, Boolean> notification() {
        int random = (int) Math.ceil(Math.random() * 10);
        boolean isNotified = random >= 2;
        Map<String, Boolean> response = new HashMap<>();
        response.put("message", isNotified);
        return response;
    }
}
