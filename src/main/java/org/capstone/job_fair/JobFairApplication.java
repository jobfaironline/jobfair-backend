package org.capstone.job_fair;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.capstone.job_fair.utils.AwsUtil;
import org.capstone.job_fair.utils.DomainUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;


@SpringBootApplication
@RestController
@Import(SpringDataRestConfiguration.class)
@EnableAsync
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

//    @Bean
//    public AWSCognitoIdentityProvider cognitoIdentityProvider() {
//        return AWSCognitoIdentityProviderClientBuilder.defaultClient();}


    public static void main(String[] args) {
        SpringApplication.run(JobFairApplication.class, args);
    }


}
