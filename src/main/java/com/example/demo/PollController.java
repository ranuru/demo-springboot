package com.example.demo;


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

    public static class CreatePollRequest {
        public Long userId;
        public String question;
        public Instant validUntil;
        public List<String> options;
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody CreatePollRequest req) {
        Optional<User> creator = Optional.ofNullable(pollManager.getUsers().get(req.userId));
        if (creator.isEmpty()) {
            throw new RuntimeException("User not found: "  + req.userId);
        }
        pollManager.createPoll(creator.get(),
                req.question,
                req.validUntil,
                req.options);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Collection<Poll> getPolls() {
        return pollManager.getPolls().values();
    }

    public static class DeletePollRequest {
        public Long id;

    }

    @DeleteMapping
    public ResponseEntity<Poll> deletePoll(@RequestBody DeletePollRequest req) {
        pollManager.deletePoll(req.id);
        return ResponseEntity.ok().build();
    }
}
