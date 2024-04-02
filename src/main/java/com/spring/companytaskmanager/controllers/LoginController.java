package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.UserInfoEntity;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.services.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class LoginController {
    private PasswordService passwordService;
    private UserInfoRepository userInfoRepository;

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @GetMapping("/recover")
    public ModelAndView getRecoverPassword() {
        return new ModelAndView("recoverpassword");
    }

    @PostMapping("/recover")
    @ResponseBody
    public ResponseEntity postRecoverPassword(@RequestParam("username") String username) {
        UserInfoEntity user = userInfoRepository.findById(username).orElse(null);

        if (user != null) passwordService.recoverPassword(user);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/login?recovered").build();
    }
}
