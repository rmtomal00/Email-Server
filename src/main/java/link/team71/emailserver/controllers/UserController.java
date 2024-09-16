package link.team71.emailserver.controllers;

import link.team71.emailserver.middleware.UserDetailsProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @GetMapping("/")
    public ResponseEntity<?> getUser() {
        System.out.println( new UserDetailsProvider().getUserDetails());
        return ResponseEntity.ok("Hello World!");
    }
}
