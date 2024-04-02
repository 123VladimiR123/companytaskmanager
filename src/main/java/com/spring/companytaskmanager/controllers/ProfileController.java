package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.DepartmentEntity;
import com.spring.companytaskmanager.entities.UserInfoEntity;
import com.spring.companytaskmanager.enums.Roles;
import com.spring.companytaskmanager.reps.DepartmentRepository;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.services.PasswordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserInfoRepository repository;
    private final DepartmentRepository departmentRepository;
    private final PasswordService passwordService;

    @GetMapping
    public ModelAndView profile(Principal principal,
                                Authentication authentication,
                                @RequestParam(name = "first", required = false) String firstName,
                                @RequestParam(name = "last", required = false) String lastName,
                                @RequestParam(name = "father", required = false) String fatherName) {

        Map<String, Object> map = new HashMap<>(1);

        map.put("user", repository.findById(principal.getName()).get());
        map.put("userRequested", (firstName == null && lastName == null) ? repository.findById(principal.getName()).get()
                : repository.findByFirstNameAndLastNameAndFatherName(firstName, lastName, fatherName));
        map.put("isAdmin", (authentication.getAuthorities().stream().map(e -> e.getAuthority())
                .collect(Collectors.toSet())).contains(Roles.ADMIN.getRole()));
        map.put("isSelf", (lastName == null || map.get("userRequested").equals(map.get("user"))));
        return new ModelAndView("profile", map);
    }

    @PatchMapping
    @ResponseBody
    public ResponseEntity setMainUser(@RequestParam(name = "first") String firstName,
                                      @RequestParam(name = "last") String lastName,
                                      @RequestParam(name = "father", required = false) String fatherName) {
        UserInfoEntity user = repository.findByFirstNameAndLastNameAndFatherName(firstName, lastName, fatherName);
        DepartmentEntity department = user.getDepartmentName();
        department.setMainUser(user);

        departmentRepository.save(department);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile").build();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity disableUser(@RequestParam(name = "first") String firstName,
                                      @RequestParam(name = "last") String lastName,
                                      @RequestParam(name = "father", required = false) String fatherName) {
        UserInfoEntity user = repository.findByFirstNameAndLastNameAndFatherName(firstName, lastName, fatherName);

        passwordService.disableUser(user);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/profile").build();
    }
}
