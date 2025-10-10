package no.hvl.dat250.manager;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import no.hvl.dat250.domain.Poll;
import no.hvl.dat250.domain.User;
import no.hvl.dat250.domain.Vote;
import no.hvl.dat250.domain.VoteOption;
import no.hvl.dat250.dto.VoteEvent;

@Component
@Data
@AllArgsConstructor
public class PollManager {

    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Vote> votes = new HashMap<>();

    private final AtomicLong userIdGen = new AtomicLong(1);
    private final AtomicLong voteIdGen = new AtomicLong(1);
    private final AtomicLong pollIdGen = new AtomicLong(1);

    private final AmqpAdmin amqpAdmin;
    private final RabbitTemplate rabbitTemplate;
    private SimpleMessageListenerContainer dynamicListenerContainer;

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
        poll.setCreatedBy(creator);

        List<VoteOption> voteOptions = new ArrayList<>();
        int presentationOrder = 0;

        final AtomicLong optionIdGen = new AtomicLong(1);

        for (String option : options) {
            VoteOption voteOption = new VoteOption();
            voteOption.setId(optionIdGen.getAndIncrement());
            voteOption.setCaption(option);
            voteOption.setPresentationOrder(presentationOrder);
            voteOptions.add(voteOption);

            presentationOrder++;
        }
        poll.setOptions(voteOptions);
        polls.put(poll.getId(), poll);

        // --- RabbitMQ setup ---
        String queueName = "poll." + poll.getId();

        TopicExchange exchange = new TopicExchange("polls-exchange");
        amqpAdmin.declareExchange(exchange);

        Queue queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);

        Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName);
        amqpAdmin.declareBinding(binding);

        if (dynamicListenerContainer != null) {
            dynamicListenerContainer.addQueueNames(queueName);
        }

        rabbitTemplate.convertAndSend("polls-exchange", queueName, "New poll created: " + poll.getQuestion());

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
            if (vote.getUserId() == user.getId() && vote.getPollId() == poll.getId()) {
                votes.remove(vote.getVoteId());
            }
        }
        newVote.setVoteOptionId(option.getId());
        for (int i = 0; i < poll.getOptions().size(); i++) {
            if (poll.getOptions().get(i) == option) {
                option.getVotes().add(newVote);
            }
        }
        votes.put(newVote.getVoteId(), newVote);

        VoteEvent voteEvent = new VoteEvent();
        voteEvent.setPollId(poll.getId());
        voteEvent.setOptionId(option.getId());
        voteEvent.setUserId(user.getId());

        // send to exchange using routing key = queueName
        rabbitTemplate.convertAndSend("polls-exchange", "poll." + poll.getId(), voteEvent);

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
