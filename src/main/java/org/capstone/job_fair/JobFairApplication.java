package org.capstone.job_fair;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.DomainUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;


@SpringBootApplication
@RestController
@Import(SpringDataRestConfiguration.class)
@EnableAsync
@EnableScheduling
public class JobFairApplication {

    @Bean
    public DomainUtil domainUtil() {
        return new DomainUtil();
    }

    @Bean
    public AwsUtil awsUtil() {
        return new AwsUtil();
    }

    @Bean
    public AmazonDynamoDB dynamoClient() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        return client;
    }

    @Bean
    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.defaultClient();
    }

    @Value("${job-hub.environment}")
    private String environment;

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig
                .Builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(environment))
                .build();
        return mapperConfig;
    }


    public static void main(String[] args) {
        SpringApplication.run(JobFairApplication.class, args);
    }


}
