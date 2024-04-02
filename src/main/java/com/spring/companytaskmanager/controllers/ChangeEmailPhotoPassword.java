package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.UserInfoEntity;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.services.PasswordService;
import com.spring.companytaskmanager.services.SaveAttachService;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ChangeEmailPhotoPassword {
    private UserInfoRepository userInfoRepository;
    private SaveAttachService saveAttachService;
    private PasswordService passwordService;

    @GetMapping("/email")
    public ModelAndView getEmailChange(Principal principal) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfoRepository.findById(principal.getName()).get());

        return new ModelAndView("emailchange", map);
    }

    @GetMapping("/photo")
    public ModelAndView getPhotoChange(Principal principal) {

        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfoRepository.findById(principal.getName()).get());

        return new ModelAndView("photochange", map);
    }

    @GetMapping("/password")
    public ModelAndView getPasswordChange(Principal principal) {

        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfoRepository.findById(principal.getName()).get());
        return new ModelAndView("changepassword", map);
    }

    @PostMapping("/photo")
    @ResponseBody
    public ResponseEntity photoChange(Principal principal,
                                      @RequestParam("photo") MultipartFile multipartFile) {

        UserInfoEntity user = userInfoRepository.findById(principal.getName()).get();
        user.setIconPath(saveAttachService.saveNewIcon(multipartFile));
        userInfoRepository.save(user);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile").build();
    }

    @PostMapping("/password")
    @ResponseBody
    public ResponseEntity passwordChange(Principal principal,
                                         @RequestParam("oldpass") String old,
                                         @RequestParam("new1") String new1,
                                         @RequestParam("new2") String new2) {

        UserInfoEntity user = userInfoRepository.findById(principal.getName()).get();

        try {
            passwordService.changePassword(user, new1, new2, old);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile/password?msg").build();
        }

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile").build();
    }

    @PostMapping("/email")
    @ResponseBody
    public ResponseEntity emailChange(Principal principal,
                                              @RequestParam("newmail1") @Email String mail1,
                                              @RequestParam("newmail2") @Email String mail2) {

        if (!mail1.equals(mail2)) return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile/email?error").build();

        UserInfoEntity user = userInfoRepository.findById(principal.getName()).get();

        try {
            passwordService.changeEmail(user, mail1);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile/email?error").build();
        }

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile/verificate?sended").build();
    }

    @GetMapping("/verificate")
    public ModelAndView getVerificateEmail(Principal principal) {

        Map<String, Object> map = new HashMap<>();
        map.put("user" ,userInfoRepository.findById(principal.getName()).get());
        return new ModelAndView("emailverificate", map);
    }

    @PostMapping("/verificate")
    @ResponseBody
    public ResponseEntity verificateEmail(Principal principal,
                                      @RequestParam("key") String key) {
        UserInfoEntity user = userInfoRepository.findById(principal.getName()).get();

        try {
            passwordService.verifyEmail(user, key);
        } catch (UsernameNotFoundException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile/verificate?error").build();
        }

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/logout").build();
    }
}
