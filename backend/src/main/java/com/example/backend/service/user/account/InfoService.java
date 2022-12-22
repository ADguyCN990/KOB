package com.example.backend.service.user.account;

import java.util.Map;

public interface InfoService {
    //根据token返回用户信息
    public Map<String, String> getInfo();
}
