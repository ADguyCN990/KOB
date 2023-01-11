package com.example.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;

public interface GetRanklistService {
    public JSONObject getList(Integer page);
}
