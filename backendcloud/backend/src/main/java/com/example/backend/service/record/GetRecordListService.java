package com.example.backend.service.record;

import com.alibaba.fastjson2.JSONObject;

public interface GetRecordListService {
    public JSONObject getList(Integer page);
}
