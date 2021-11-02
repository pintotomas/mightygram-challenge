package com.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {
    //Returns file path
    void store(MultipartFile file, String filename);
    Path load(String filename);
    Resource loadAsResource(String filename);
    void init();
}
