package br.com.anthony.picpay.authorization;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/authorization")
public class AuthorizationController {

    @GetMapping
    public Map<String, String> authorization() {
        double random = Math.random();
        String isAuthorized = random % 2 == 0 ? "Authorized" : "Rejected";
        Map<String, String> response = new HashMap<>();
        response.put("message", isAuthorized);
        return response;
    }
}
