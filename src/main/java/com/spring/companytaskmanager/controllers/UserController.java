package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.DTO.UserDTO;
import com.spring.companytaskmanager.entities.DepartmentEntity;
import com.spring.companytaskmanager.entities.UserInfoEntity;
import com.spring.companytaskmanager.enums.Roles;
import com.spring.companytaskmanager.reps.DepartmentRepository;
import com.spring.companytaskmanager.reps.UserInfoPagingAndSorting;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.services.PasswordService;
import com.spring.companytaskmanager.staticnotservices.QueryConverter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserInfoPagingAndSorting userInfoPagingAndSorting;
    private final UserInfoRepository userInfoRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordService passwordService;

    @GetMapping
    public ModelAndView getUsers(Principal principal,
                                 Authentication authentication,
                                 @RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "page", required = false) String page) {

        Integer pageInt;
        if (page == null) pageInt = 1; else pageInt = Integer.parseInt(page);

        PageRequest firstUsers =
                PageRequest.of(pageInt - 1, 10, Sort.by("last_name").ascending());

        Page<UserInfoEntity> list = userInfoPagingAndSorting.findByQuery(QueryConverter.convert(query), firstUsers);

        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("list", list);
        map.put("query", query);
        map.put("page", pageInt);
        map.put("isAdmin", authentication.getAuthorities().contains(new SimpleGrantedAuthority(Roles.ADMIN.getRole())));

        return new ModelAndView("users", map);
    }

    @GetMapping("/create")
    public ModelAndView getCreateUser(Principal principal) {


        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfoRepository.findById(principal.getName()).get());

        return new ModelAndView("userscreate", map);
    }

    @PatchMapping("/create")
    public ModelAndView getUpdateUser(Principal principal,
                                      @RequestParam("email") @Email String email) {
        UserInfoEntity userInfoEntity = userInfoRepository.findById(email).orElse(null);

        Map<String, Object> map = new HashMap<>();

        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("tocreate", userInfoEntity);
        map.put("departments", departmentRepository.findAllByEnabledIsTrue());
        map.put("email", email);

        return new ModelAndView("userdata", map);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity created(@Valid UserDTO newUser) {
        passwordService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/users").build();
    }
}
