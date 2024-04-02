package com.spring.companytaskmanager.controllers;

import com.spring.companytaskmanager.entities.PostEntity;
import com.spring.companytaskmanager.reps.AttachmentRepository;
import com.spring.companytaskmanager.reps.PostRepository;
import com.spring.companytaskmanager.reps.UserInfoRepository;
import com.spring.companytaskmanager.services.SaveAttachService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class FeedPostController {
    private PostRepository postRepository;
    private UserInfoRepository userInfoRepository;
    private AttachmentRepository attachmentRepository;
    private SaveAttachService saveAttachService;

    @GetMapping("/post/create")
    public ModelAndView createPost(Principal principal) {
        Map<String, Object> map = new HashMap<>();
        map.put("user",  userInfoRepository.findById(principal.getName()).get());
        return new ModelAndView("createpost", map);
    }

    @Transactional
    @PostMapping("/post/create")
    public ModelAndView createPostSent(@RequestParam("message") String message,
                                       @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                       Principal principal)
    throws ArrayIndexOutOfBoundsException {

        String postId = "post-" + UUID.randomUUID();

        PostEntity post = PostEntity.builder()
                .postId(postId)
                .published(Timestamp.valueOf(LocalDateTime.now()))
                .fromUser(userInfoRepository.findById(principal.getName()).orElseThrow())
                .message(message)
                .build();

        postRepository.save(post);

        saveAttachService.saveAll(files, post.getPostId());

        return new ModelAndView("redirect:/");
    }
}
