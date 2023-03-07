package com.example.backend.controller.user.account;

import com.example.backend.service.user.account.ShowphotoService;
import com.example.backend.utils.PhotoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowphotoController {
    @Autowired
    private ShowphotoService showphotoService;
    @GetMapping("/api/user/account/showphoto/")
    public List<PhotoUtil> showphoto() {
        return showphotoService.showphoto();
    }
}
