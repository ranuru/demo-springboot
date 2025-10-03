package no.hvl.dat250.cache;

import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisCache {

    private final JedisPool jedisPool;
    private final long ttlSeconds = 60 * 10;

    public RedisCache(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Map<String, String> getPollResults(String pollId) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll("poll:" + pollId + ":votes");
        }
    }

    public void setPollResults(String pollId, Map<String, String> votes) { 
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset("poll:" + pollId + ":votes", votes);
            
            jedis.expire("poll:" + pollId + ":votes", ttlSeconds);
        }
    }
}