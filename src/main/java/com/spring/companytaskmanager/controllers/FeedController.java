package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.CommentEntity;
import com.spring.companytaskmanager.entities.PostEntity;
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
public class FeedController {
    private CommentsPagingAndSorting commentsPagingAndSorting;
    private PostPagingAndSorting postPagingAndSorting;
    private PostRepository postRepository;
    private UserInfoRepository userInfoRepository;
    private CommentRepository commentRepository;
    private SaveAttachService saveAttachService;

    @GetMapping("/")
    public ModelAndView getFeed(Principal principal,
                                @RequestParam(value = "query", required = false) String query,
                                @RequestParam(value = "page", required = false) String page) {
        Integer pageInt;
        if (page == null) pageInt = 1; else pageInt = Integer.parseInt(page);

        PageRequest firstPosts =
                PageRequest.of(pageInt - 1, 10, Sort.by("date_published").descending());

        PageRequest firstComments =
                PageRequest.of(0, 3, Sort.by("published").descending());

        Page<PostEntity> list = postPagingAndSorting.findByQuery(QueryConverter.convert(query), firstPosts);
        list.getContent().forEach(post ->
                post.setCommentsPreview(commentsPagingAndSorting.findCommentEntitiesByOwner(post.getPostId(), firstComments)));

        Map<String, Object> map = new HashMap<>();
        map.put("posts", list);
        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("query", (query == null) ? "" : query);
        map.put("page", pageInt);
        return new ModelAndView("feed", map);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity getCommentsTo(Principal principal,
                                        @RequestParam("postid") String postId,
                                        @RequestParam("message") String message,
                                        @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        CommentEntity comment = CommentEntity.builder()
                .commentId("comm-" + UUID.randomUUID())
                .published(Timestamp.valueOf(LocalDateTime.now()))
                .fromUser(userInfoRepository.findById(principal.getName()).orElse(null))
                .message(message)
                .owner(postId)
                .build();

        commentRepository.save(comment);

        saveAttachService.saveAll(files, comment.getCommentId());

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/").build();
    }

    @GetMapping("post/{id}")
    public ModelAndView getAllComments(Principal principal,
                                       @PathVariable String id) {
        PostEntity postEntity = postRepository.findById(id).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByOwner(id);

        Map<String, Object> map = new HashMap<>();

        map.put("user", userInfoRepository.findById(principal.getName()).get());
        map.put("post", postEntity);
        map.put("comments", commentEntityList);

        return new ModelAndView("postcomments", map);
    }
}
