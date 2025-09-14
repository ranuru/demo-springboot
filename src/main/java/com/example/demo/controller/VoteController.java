package com.example.demo.controller;

import com.example.demo.dto.VoteRequest;
import com.example.demo.manager.PollManager;
import com.example.demo.domain.Poll;
import com.example.demo.domain.User;
import com.example.demo.domain.Vote;
import com.example.demo.domain.VoteOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/votes")
@CrossOrigin
public class VoteController {

    private final PollManager pollManager;

    @Autowired
    public VoteController (PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping
    public ResponseEntity<Poll> addOrUpdateVote(@RequestBody VoteRequest req) {
        // anonymous voting
        User anonymous = new User();
        Long userId = req.getUserId();
        Long pollId = req.getPollId();
        Long optionId = req.getOptionId();


        Optional<User> user = Optional.ofNullable(pollManager.getUsers().get(userId));
        Poll poll = pollManager.getPolls().get(pollId);

    for (VoteOption option : poll.getOptions()) {
        if (option.getId() == optionId && user.isPresent()) {
            poll = pollManager.addOrUpdateVote(user.get(), poll, option);
            break;
        }
        else if (option.getId() == optionId) {
            poll = pollManager.addOrUpdateVote(anonymous, poll, option);
            break;
    }
    }
    return  ResponseEntity.ok(poll);
}
    @GetMapping
    public Collection<Vote> getVotes() {
        return pollManager.getVotes().values();
    }
}
