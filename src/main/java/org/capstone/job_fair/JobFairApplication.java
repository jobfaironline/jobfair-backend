package org.capstone.job_fair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class JobFairApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobFairApplication.class, args);
    }


}
