package com.example.demo.manager;


import com.example.demo.domain.Poll;
import com.example.demo.domain.User;
import com.example.demo.domain.Vote;
import com.example.demo.domain.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Data
@AllArgsConstructor
public class PollManager {

    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, User>  users = new HashMap<>();
    private final Map<Long, Vote>  votes = new HashMap<>();

    private final AtomicLong userIdGen = new AtomicLong(1);
    private final AtomicLong voteIdGen = new AtomicLong(1);
    private final AtomicLong pollIdGen = new AtomicLong(1);


    public ResponseEntity<User> createUser(String username, String email) {
        User user = new User();
        user.setId(userIdGen.getAndIncrement());
        user.setUsername(username);
        user.setEmail(email);
        users.put(user.getId(), user);
        return ResponseEntity.ok(user);
    }

    public Poll createPoll(User creator, String question, Instant validUntil, List<String> options) {
        Poll poll = new Poll();
        poll.setId(pollIdGen.getAndIncrement());
        poll.setPublishedAt(Instant.now());
        poll.setQuestion(question);
        poll.setValidUntil(validUntil);
        poll.setCreator(creator);

        List<VoteOption> voteOptions = new ArrayList<>();
        int presentationOrder = 0;

        final AtomicLong optionIdGen = new AtomicLong(1);

        for (String option : options) {
            VoteOption voteOption = new VoteOption();
            voteOption.setId(optionIdGen.getAndIncrement());
            voteOption.setCaption(option);
            voteOption.setPresentationOrder(presentationOrder);
            voteOption.setVotes(0);
            voteOptions.add(voteOption);

            presentationOrder++;
        }
        poll.setOptions(voteOptions);
        polls.put(poll.getId(), poll);
        return poll;
    }

    public Poll addOrUpdateVote(User user, Poll poll, VoteOption option) {
        Vote newVote = new Vote();
        newVote.setPublishedAt(Instant.now());
        newVote.setUserId(user.getId());
        newVote.setPollId(poll.getId());
        newVote.setVoteId(voteIdGen.getAndIncrement());
        List<Vote> oldVotes = votes.values().stream().toList();
        for (Vote vote : oldVotes) {
            if (vote.getUserId() == user.getId() && vote.getPollId() ==  poll.getId()) {
                votes.remove(vote.getVoteId());
            }
        }
        newVote.setVoteOptionId(option.getId());
        for (int i = 0; i < poll.getOptions().size(); i++) {
            if (poll.getOptions().get(i) == option) {
                option.setVotes(option.getVotes() + 1);
            }
        }
        votes.put(newVote.getVoteId(), newVote);
        return poll;
    }

    public void deletePoll(Long pollId) {
        polls.remove(pollId);
        List<Vote> oldVotes = votes.values().stream().toList();
        for (Vote vote : oldVotes) {
            if (vote.getPollId() == pollId) {
                votes.remove(vote.getVoteId());
            }
        }
    }

 }
