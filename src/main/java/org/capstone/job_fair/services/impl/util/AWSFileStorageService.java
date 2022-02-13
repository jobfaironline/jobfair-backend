package org.capstone.job_fair.services.impl.util;

import lombok.SneakyThrows;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class AWSFileStorageService implements FileStorageService {

    @Autowired
    private S3Client amazonS3Client;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Override
    @SneakyThrows
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> store(byte[] bytes, String name) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("x-amz-meta-myVal", "test");

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(name)
                .metadata(metadata)
                .build();


        PutObjectResponse response = amazonS3Client.putObject(putOb, RequestBody.fromBytes(bytes));
        return CompletableFuture.completedFuture(null);
    }


    @Override
    @SneakyThrows
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Resource> loadAsResource(String filename) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = amazonS3Client.getObjectAsBytes(objectRequest);
        return CompletableFuture.completedFuture(new ByteArrayResource(objectBytes.asByteArray()));
    }
}
