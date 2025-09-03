package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PollManager pollManager;

    public UserController (PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        pollManager.createUser(user.getUsername(), user.getEmail());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return pollManager.getUsers().values();
    }

}
