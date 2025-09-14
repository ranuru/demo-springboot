package com.example.demo.controller;

import com.example.demo.manager.PollManager;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final PollManager pollManager;

    @Autowired
    public UserController (PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return pollManager.createUser(user.getUsername(), user.getEmail());
    }

    @GetMapping
    public Collection<User> getUsers() {
        return pollManager.getUsers().values();
    }

}
