package com.spring.companytaskmanager.services;

import com.spring.companytaskmanager.entities.AttachmentEntity;
import com.spring.companytaskmanager.reps.AttachmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SaveAttachService {
    private AttachmentRepository attachmentRepository;

    @Transactional
    public void saveAll(List<MultipartFile> files, String ownerId) {
        Map<String, AttachmentEntity> attachmentMap = files.stream().filter(e -> !e.isEmpty()).map(e -> AttachmentEntity.builder()
                        .name(e.getOriginalFilename())
                        .owner_id(ownerId)
                        .path(UUID.randomUUID() + e.getOriginalFilename().substring(e.getOriginalFilename().lastIndexOf('.')))
                        .build())
                .collect(Collectors.toMap(AttachmentEntity::getName, e -> e));

        files.stream().filter(e -> !e.isEmpty()).forEach(f -> {
            try {
                f.transferTo(Path.of("src/main/resources/static/attachments/" + attachmentMap.get(f.getOriginalFilename()).getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if (attachmentMap.values().size() > 0) attachmentRepository.saveAll(attachmentMap.values());
    }

    @SneakyThrows
    @Transactional
    public String saveNewIcon(MultipartFile file) {
        List<String> allowedIcons = List.of(".jpg", ".jpeg", ".png");
        String dep = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));

        if (!allowedIcons.contains(dep)) throw new IllegalArgumentException();

        String name = UUID.randomUUID() + dep;

        file.transferTo(Path.of("src/main/resources/static/images/" + name));
        return name;
    }
}
