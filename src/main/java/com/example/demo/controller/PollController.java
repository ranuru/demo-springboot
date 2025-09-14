package com.example.demo.controller;


import com.example.demo.dto.CreatePollRequest;
import com.example.demo.manager.PollManager;
import com.example.demo.domain.Poll;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/polls")
@CrossOrigin
public class PollController
{
    private final PollManager pollManager;

    @Autowired
    public PollController(PollManager pollManager)
    {
        this.pollManager = pollManager;
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody CreatePollRequest req) {

        Long userId = req.getUserId();
        String question = req.getQuestion();
        Instant validUntil = req.getValidUntil();
        List<String> options =  req.getOptions();

        Optional<User> creator = Optional.ofNullable(pollManager.getUsers().get(userId));
        if (creator.isEmpty()) {
            throw new RuntimeException("User not found: "  + userId);
        }
        Poll poll = pollManager.createPoll(creator.get(), question, validUntil, options);
        return ResponseEntity.ok(poll);
    }

    @GetMapping
    public Collection<Poll> getPolls() {
        Collection<Poll> polls = pollManager.getPolls().values();
        System.out.println("Returning polls:" + polls);
        return polls;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        pollManager.deletePoll(id);
        return ResponseEntity.ok().build();
    }

}
