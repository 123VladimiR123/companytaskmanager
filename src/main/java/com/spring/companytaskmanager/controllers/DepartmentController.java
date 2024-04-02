package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.DepartmentEntity;
import com.spring.companytaskmanager.enums.Roles;
import com.spring.companytaskmanager.reps.DepartmentPagingAndSorting;
import com.spring.companytaskmanager.reps.DepartmentRepository;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.staticnotservices.QueryConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/departments")
@AllArgsConstructor
public class DepartmentController {
    private DepartmentPagingAndSorting departmentPagingAndSorting;
    private DepartmentRepository departmentRepository;
    private UserInfoRepository userInfoRepository;

    @GetMapping
    public ModelAndView getDepartments(Principal principal,
                                       Authentication authentication,
                                       @RequestParam(value = "query", required = false) String query,
                                       @RequestParam(value = "page", required = false) String page) {
        Integer pageInt;
        if (page == null) pageInt = 1; else pageInt = Integer.parseInt(page);

        Page<DepartmentEntity> list = departmentPagingAndSorting.findByQuery(QueryConverter.convert(query),
                PageRequest.of(pageInt - 1, 25,
                        Sort.by(Sort.Order.desc("enabled"),
                        Sort.Order.asc("department_name"))));

        Map<String, Object> map = new HashMap<>();
        map.put("departments", list);
        map.put("query", query);
        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("page", pageInt);
        map.put("isAdmin", (authentication.getAuthorities().stream().map(e -> e.getAuthority())
                .collect(Collectors.toSet())).contains(Roles.ADMIN.getRole()));

        return new ModelAndView("departments", map);
    }

    @PostMapping
    public ModelAndView deleteDepartment(Principal principal,
                                         Authentication authentication,
                                         @RequestParam(value = "query", required = false) String query,
                                         @RequestParam(value = "page", required = false) String page,
                                         @RequestParam(value = "name") String name) {

        DepartmentEntity entity = departmentRepository.findById(name).get();
        entity.setEnabled(!entity.isEnabled());
        departmentRepository.save(entity);

        return getDepartments(principal, authentication, query, page);
    }

    @PostMapping("/create")
    public ModelAndView postCreateDepartment(Principal principal,
                                             Authentication authentication,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "owner", required = false) String owner) {
        departmentRepository.save(
            DepartmentEntity.builder()
                    .enabled(true)
                    .name(name)
                    .departmentHigher(departmentRepository.findById(owner).orElse(null))
                    .build()
        );

        return new ModelAndView("redirect:/departments");
    }

    @GetMapping("/create")
    public ModelAndView createDepartment(Principal principal,
                                         Authentication authentication) {

        Map<String, Object> map = new HashMap<>();
        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("departments", departmentRepository.findAll());

        return new ModelAndView("createdepartment", map);
    }
}
