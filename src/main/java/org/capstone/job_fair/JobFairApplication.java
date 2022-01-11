package org.capstone.job_fair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;


@SpringBootApplication
@RestController
@Import(SpringDataRestConfiguration.class)
public class JobFairApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobFairApplication.class, args);
        System.out.print("Hello world");
    }


}
