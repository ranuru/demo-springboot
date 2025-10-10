package no.hvl.dat250.controller;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.domain.Poll;
import no.hvl.dat250.domain.User;
import no.hvl.dat250.dto.CreatePollRequest;
import no.hvl.dat250.manager.PollManager;

@RestController
@RequestMapping("/polls")
@CrossOrigin
public class PollController {
    private final PollManager pollManager;

    @Autowired
    public PollController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody CreatePollRequest req) {

        Long userId = req.getUserId();
        String question = req.getQuestion();
        Instant validUntil = req.getValidUntil();
        List<String> options = req.getOptions();

        Optional<User> creator = Optional.ofNullable(pollManager.getUsers().get(userId));
        if (creator.isEmpty()) {
            throw new RuntimeException("User not found: " + userId);
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
