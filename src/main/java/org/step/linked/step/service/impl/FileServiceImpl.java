package org.step.linked.step.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.step.linked.step.model.User;
import org.step.linked.step.service.FileService;
import org.step.linked.step.service.UserService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final UserService userService;

    @Value("${store.directory}")
    private String directory;
    private Path path;

    @Autowired
    public FileServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.path = Paths
                .get(directory)
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(String.format("File directory %s cannot be created", path.toString()));
        }
    }

    @Override
    public String save(InputStream io, String originalFilename, String userID) throws IOException {
        User userByID = userService.findById(userID);

        String filename = UUID.randomUUID().toString() + originalFilename;

        Path resolvedPath = path.resolve(filename);

        Files.copy(io, resolvedPath, StandardCopyOption.REPLACE_EXISTING);

        userByID.setFilename(filename);

        userService.update(userByID);

        return filename;
    }

    @Override
    public Resource download(String filename) {
        Path resolvedPath = path.resolve(filename);
        return new FileSystemResource(resolvedPath);
    }
}
