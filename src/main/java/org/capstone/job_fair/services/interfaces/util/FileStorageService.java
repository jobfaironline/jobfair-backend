package org.capstone.job_fair.services.interfaces.util;

import org.springframework.core.io.Resource;

import java.util.concurrent.CompletableFuture;

public interface FileStorageService {
    void store(byte[] bytes, String name);

    Resource loadAsResource(String filename);
}
