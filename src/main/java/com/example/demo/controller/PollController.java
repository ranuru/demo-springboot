package com.example.demo.controller;


import com.example.demo.dto.CreatePollRequest;
import com.example.demo.dto.DeletePollRequest;
import com.example.demo.manager.PollManager;
import com.example.demo.domain.Poll;
import com.example.demo.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/polls")
public class PollController
{
    private final PollManager pollManager;

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
        pollManager.createPoll(creator.get(),
                question,
                validUntil,
                options);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Collection<Poll> getPolls() {
        return pollManager.getPolls().values();
    }

    @DeleteMapping
    public ResponseEntity<Poll> deletePoll(@RequestBody DeletePollRequest req) {
        pollManager.deletePoll(req.getId());
        return ResponseEntity.ok().build();
    }
}
