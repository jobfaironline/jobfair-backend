package org.capstone.job_fair.services.impl.util;

import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AWSFileStorageService implements FileStorageService {

    //TODO: implement AWS code

    @Override
    public void store(MultipartFile file, String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource loadAsResource(String filename) {
        throw new UnsupportedOperationException();
    }
}
