package org.capstone.job_fair.services.interfaces.util;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void store(MultipartFile file, String name);

    Resource loadAsResource(String filename);
}
