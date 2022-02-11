package org.capstone.job_fair.services.interfaces.util;

import org.springframework.core.io.Resource;

import java.util.concurrent.CompletableFuture;

public interface FileStorageService {
    CompletableFuture<Void> store(byte[] bytes, String name);

    CompletableFuture<Resource> loadAsResource(String filename);
}
