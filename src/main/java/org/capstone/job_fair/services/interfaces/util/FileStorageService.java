package org.capstone.job_fair.services.interfaces.util;

import org.springframework.core.io.Resource;

public interface FileStorageService {
    void store(byte[] bytes, String name);

    Resource loadAsResource(String filename);
}
