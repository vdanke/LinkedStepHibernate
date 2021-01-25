package org.step.linked.step.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.step.linked.step.model.User;
import org.step.linked.step.service.FileService;
import org.step.linked.step.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class FileDBServiceImpl implements FileService {

    private final UserService userService;

    @Autowired
    public FileDBServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String save(InputStream io, String originalFilename, String userID) throws IOException {
        byte[] bytes = io.readAllBytes();
        User user = userService.findById(userID);
        String file = Base64.getEncoder().encodeToString(bytes);
        user.setFile(file);
        user.setFilename(userID + originalFilename);
        userService.update(user);
        return originalFilename;
    }

    @Override
    public Resource download(String filename) {
        String file = userService.fileFileByFilename(filename);
        byte[] fileBytes = Base64.getDecoder().decode(file);
        return new ByteArrayResource(fileBytes);
    }
}
