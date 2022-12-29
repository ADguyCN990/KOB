package com.example.matchingsystem.service.impl;

import com.example.matchingsystem.service.MatchingService;
import com.example.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating, Integer bodId) {
        System.out.println("add player: " + userId + " " + rating + " " + bodId);
        matchingPool.addPlayer(userId, rating, bodId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
