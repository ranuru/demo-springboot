package no.hvl.dat250;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import redis.clients.jedis.UnifiedJedis;

@RestController
@SpringBootApplication
public class ApplicationTemplateApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ApplicationTemplateApplication.class, args);
		UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

//         SELECT o.presentationOrder, COUNT(v.id)
// FROM vote_options o 
// INNER JOIN votes v on o.id = v.voted_on 
// WHERE o.poll = ?
// GROUP BY o.presentationOrder
// ORDER BY o.presentationOrder;

		String luaScript = "local pollId = ARGV[1] " +
				"local result = {} " +
				"local options = redis.call('ZRANGE', 'poll:' .. pollId .. ':options', 0, -1) " +
				"for i, optionId in ipairs(options) do " +
				"  local voteCount = redis.call('SCARD', 'option:' .. optionId .. ':votes') " +
				"  table.insert(result, {optionId, voteCount}) " +
				"end " +
				"return result";
		String pollId = "1"; // Replace with the actual poll ID you want to query
		Object result = jedis.eval(luaScript, 0, pollId);
		System.out.println(result);
		
        jedis.close();
    }
}


	

