package com.cvp.skuska.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String LEADERBOARD_KEY = "leaderboard";

    // Increment user's score and update the leaderboard
    public void incrementUserScore(String userId, int increment) {
        redisTemplate.opsForZSet().incrementScore(LEADERBOARD_KEY, userId, increment);
    }

    // Get user's current rank (Redis ranks start from 0)
    public Long getUserRank(String userId) {
        Long rank = redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, userId);
        return (rank != null) ? rank + 1 : null; // Convert to 1-based rank
    }

    // Get the leaderboard (top N users)
    public Set<Object> getTopUsers(int topN) {
        return Collections.singleton(redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, topN - 1));
    }

    // Get user's current score
    public Double getUserScore(String userId) {
        return redisTemplate.opsForZSet().score(LEADERBOARD_KEY, userId);
    }
}
