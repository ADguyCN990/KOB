package com.example.backend.controller.user.account;

import com.example.backend.service.user.account.GetphotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetphotoController {
    @Autowired
    private GetphotoService getphotoService;

    @PostMapping("/user/account/getphoto/")
    public Map<String, String> getPhoto(@RequestParam Map<String, String> data) {
        return getphotoService.getPhoto(data);
    }
}
