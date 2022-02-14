package org.capstone.job_fair.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3ClientConfig {

    @Value("${amazonProperties.region}")
    private String regionValue;

    @Bean
    public S3Client awsClient() {
        Region region = Region.of(regionValue);
        return S3Client.builder().region(region).build();
    }
}
