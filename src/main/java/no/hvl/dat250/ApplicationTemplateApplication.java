package no.hvl.dat250;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.domain.Poll;
import no.hvl.dat250.domain.Vote;
import no.hvl.dat250.domain.VoteOption;
import no.hvl.dat250.manager.PollManager;
import redis.clients.jedis.UnifiedJedis;

@RestController
@SpringBootApplication
public class ApplicationTemplateApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ApplicationTemplateApplication.class, args);
		UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

		// use case 1 keep track of logged-in users
		jedis.del("loggedIn");

		jedis.sadd("loggedIn", "alice");
		System.out.println(jedis.smembers("loggedIn"));
		jedis.sadd("loggedIn", "bob");
		System.out.println(jedis.smembers("loggedIn"));
		jedis.srem("loggedIn", "alice");
		System.out.println(jedis.smembers("loggedIn"));
		jedis.sadd("loggedIn", "eve");
		System.out.println(jedis.smembers("loggedIn"));

		// use case 2 representing complex information
		jedis.hset("Poll", "id", "03ebcb7b-bd69-440b-924e-f5b7d664af7b");
		jedis.hset("Poll", "title", "Pineapple on Pizza?");

		// option 1
		jedis.hset("Poll", "options", "option1");

		jedis.hset("Options", "number", "option1");

		jedis.hset("option1", "caption", "Yes yammy");
		jedis.hset("option1", "voteCount", "269");

		// option 2
		jedis.hset("Poll", "options", "option2");

		jedis.hset("Options", "number", "option2");

		jedis.hset("option2", "caption", "Mamma mia nooooooo!");
		jedis.hset("option2", "voteCount", "268");

		// option 3
		jedis.hset("Poll", "options", "option3");

		jedis.hset("Options", "number", "option3");

		jedis.hset("option3", "caption", "I do not really care ...!");
		jedis.hset("option3", "voteCount", "42");

		// implementing cache for aggregated number of votes in each poll

		// setup /////////////////////////////////////
		PollManager pm = new PollManager();
		List<String> options = new ArrayList<>();
		options.add("y");
		options.add("n");
		options.add("m");
		Poll poll1 = pm.createPoll(null, "Is it?", null, options);

		List<Vote> votes1 = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Vote vote = new Vote();
			votes1.add(vote);
		}

		List<Vote> votes2 = new ArrayList<>();
		for (int i = 0; i < 101; i++) {
			Vote vote = new Vote();
			votes2.add(vote);
		}

		List<Vote> votes3 = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Vote vote = new Vote();
			votes3.add(vote);
		}

		poll1.getOptions().get(0).setVotes(votes1);
		poll1.getOptions().get(1).setVotes(votes2);
		poll1.getOptions().get(2).setVotes(votes3);

		////////////////////////////////////

		String pollId = poll1.getId().toString();
		String option1Id = String.valueOf(poll1.getOptions().get(0).getId());
		String option2Id = String.valueOf(poll1.getOptions().get(1).getId());
		String option3Id = String.valueOf(poll1.getOptions().get(2).getId());
		// caching aggregated vote counts for poll1
		if (!jedis.hexists(pollId, option1Id) && !jedis.hexists(pollId, option2Id)
				&& !jedis.hexists(pollId, option3Id) || !votesAreEqual(poll1, pm, jedis)) {

			jedis.hset(pollId, option1Id, "0");
			jedis.hset(pollId, option2Id, "0");
			jedis.hset(pollId, option3Id, "0");

			// expiration time //
			jedis.expire(pollId, 600);

			// incrementing vote count for option 1
			for (Vote vote : poll1.getOptions().get(0).getVotes()) {
				jedis.hincrBy(pollId, option1Id, 1);
			}
			// incrementing vote count for option 2
			for (Vote vote : poll1.getOptions().get(1).getVotes()) {
				jedis.hincrBy(pollId, option2Id, 1);
			}
			// incrementing vote count for option 3
			for (Vote vote : poll1.getOptions().get(2).getVotes()) {
				jedis.hincrBy(pollId, option3Id, 1);
			}

		}
		// retrieving aggregated vote counts
		System.out.println("Aggregated vote counts for poll id " + pollId);
		System.out.println("Option 1: " + jedis.hget(pollId, option1Id));
		System.out.println("Option 2: " + jedis.hget(pollId, option2Id));
		System.out.println("Option 3: " + jedis.hget(pollId, option3Id));

		jedis.close();

	}

	private static boolean votesAreEqual(Poll poll, PollManager pm, UnifiedJedis jedis) {
		Long pollId = poll.getId();
		for (VoteOption option : pm.getPolls().get(pollId).getOptions()) {
			String pollIdString = String.valueOf(pollId);
			String optionIdString = String.valueOf(option.getId());
			String votesJedis = jedis.hget(pollIdString, optionIdString);
			List<Vote> votesActual = pm.getPolls().get(pollId).getOptions()
					.get(pm.getPolls().get(pollId).getOptions().indexOf(option)).getVotes();
			int votesActualCount = 0;
			for (Vote vote : votesActual) {
				votesActualCount += 1;
			}
			if (!String.valueOf(votesActualCount).equals(votesJedis))
				return false;
		}
		return true;
	}
}
