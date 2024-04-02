package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.AttachmentEntity;
import com.spring.companytaskmanager.entities.CommentEntity;
import com.spring.companytaskmanager.entities.TaskEntity;
import com.spring.companytaskmanager.reps.*;
import com.spring.companytaskmanager.services.SaveAttachService;
import com.spring.companytaskmanager.staticnotservices.QueryConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private TasksPagingAndSorting tasksPagingAndSorting;
    private TaskRepository taskRepository;
    private UserInfoRepository userInfoRepository;
    private AttachmentRepository attachmentRepository;
    private CommentRepository commentRepository;
    private SaveAttachService saveAttachService;

    @GetMapping
    public ModelAndView getTasks(Principal principal,
                                 @RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "page", required = false) String page,
                                 @RequestParam(value = "qualifier", required = false) String qualifier) {
        Integer pageInt;
        if (page == null) pageInt = 1; else pageInt = Integer.parseInt(page);

        PageRequest pageRequest = PageRequest.of(
                pageInt - 1, 25,
                Sort.by(
                        Sort.Order.desc("enabled"),
                        Sort.Order.asc("name")
                )
        );

        Page<TaskEntity> list;

        switch ((qualifier == null) ? "" : qualifier) {
            case "my" :
                list = tasksPagingAndSorting.findByQueryToUser(principal.getName(), QueryConverter.convert(query), pageRequest);
                break;
            case "fromme" :
                list = tasksPagingAndSorting.findByQueryFromUser(principal.getName(), QueryConverter.convert(query), pageRequest);
                break;
            default:
                list = tasksPagingAndSorting.findByQuery(QueryConverter.convert(query), pageRequest);
        }

        Map<String, Object> map = new HashMap<>();

        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("tasks", list);
        map.put("page", pageInt);
        map.put("query", query);
        map.put("qualifier", qualifier);

        return new ModelAndView("tasks", map);
    }

    @GetMapping("/get/{id}")
    public ModelAndView getTaskById(Principal principal,
                                    @PathVariable String id) {

        TaskEntity taskEntity = tasksPagingAndSorting.findByTaskId(id);
        List<CommentEntity> commentEntities = commentRepository.findAllByOwner(id);
        commentEntities.sort(Comparator.comparing(CommentEntity::getPublished));

        Map<String, Object> map = new HashMap<>();

        map.put("task", taskEntity);
        map.put("comments", commentEntities);
        map.put("user", userInfoRepository.findById(principal.getName()).get());

        return new ModelAndView("taskById", map);
    }

    @PostMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity createComment(Principal principal,
                                        @PathVariable String id,
                                        @RequestParam("message") String message,
                                        @RequestParam(value = "files", required = false) List<MultipartFile> files){

        CommentEntity comment = CommentEntity.builder()
                .commentId("comment-" + UUID.randomUUID())
                .published(Timestamp.valueOf(LocalDateTime.now()))
                .owner(id)
                .fromUser(userInfoRepository.findById(principal.getName()).get())
                .message(message)
                .build();

        commentRepository.save(comment);

        saveAttachService.saveAll(files, comment.getCommentId());

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/tasks/get/" + id).build();
    }

    @GetMapping("/create")
    public ModelAndView createTaskGetForm(Principal principal) {

        Map<String, Object> map = new HashMap<>();

        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("list", userInfoRepository.findAll());

        return new ModelAndView("taskCreate", map);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity createTask(Principal principal,
                                     @RequestParam("message") String message,
                                     @RequestParam("name") String name,
                                     @RequestParam("owner") String owner,
                                     @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        TaskEntity task = TaskEntity.builder()
                .taskId("task-" + UUID.randomUUID())
                .fromUser(userInfoRepository.findById(principal.getName()).get())
                .name(name)
                .published(Timestamp.valueOf(LocalDateTime.now()))
                .message(message)
                .forUser(userInfoRepository.findById(owner).get())
                .enabled(true)
                .build();

        taskRepository.save(task);

        saveAttachService.saveAll(files, task.getTaskId());

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/tasks").build();
    }
}
