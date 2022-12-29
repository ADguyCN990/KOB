package com.example.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer bodId);
    String removePlayer(Integer userId);
}
