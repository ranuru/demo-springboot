package com.example.demo.controller;

import com.example.demo.manager.PollManager;
import com.example.demo.domain.User;
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
