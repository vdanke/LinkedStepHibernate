package org.step.linked.step.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String save(InputStream io, String originalFilename, String userID) throws IOException;

    Resource download(String filename);
}
