package org.capstone.job_fair;

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

    public static void main(String[] args) {
        SpringApplication.run(JobFairApplication.class, args);
    }


}
