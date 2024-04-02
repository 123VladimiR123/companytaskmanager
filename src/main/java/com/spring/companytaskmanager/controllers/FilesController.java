package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.AttachmentEntity;
import com.spring.companytaskmanager.reps.AttachmentPagingRepository;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.staticnotservices.QueryConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class FilesController {
    private AttachmentPagingRepository attachmentPagingRepository;
    private UserInfoRepository userInfoRepository;

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam(value = "path") String path) {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + path).body(new FileSystemResource("src/main/resources/static/attachments/" + path)) ;
    }

    @GetMapping("/files")
    public ModelAndView listFiles(Principal principal,
                                  @RequestParam(value = "page", required = false) String page,
                                  @RequestParam(value = "query", required = false) String query) {
        Integer pageInt;
        if (page == null) pageInt = 1; else pageInt = Integer.parseInt(page);

        Page<AttachmentEntity> list = attachmentPagingRepository.findByQuery(QueryConverter.convert(query),
                PageRequest.of(pageInt - 1, 25, Sort.by("attachment_name").ascending()));

        Map<String, Object> map = new HashMap<>();
        map.put("attachments", list);
        map.put("query", query);
        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("page", pageInt);

        return new ModelAndView("files", map);
    }

    @GetMapping("/images/{path}")
    public ResponseEntity<FileSystemResource> getPhoto(@PathVariable(value = "path") String path) {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + path).body(new FileSystemResource("src/main/resources/static/images/" + path)) ;
    }
}
