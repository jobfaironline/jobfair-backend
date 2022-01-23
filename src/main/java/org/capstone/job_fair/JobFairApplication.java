package org.capstone.job_fair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;


@SpringBootApplication
@RestController
@Import(SpringDataRestConfiguration.class)
@EnableAsync
public class JobFairApplication {

    public static void main(String[] args) {        
        System.out.print("Environment varriable: ");
        System.out.print(System.getenv("DATASOURCE_PASSWORD"));
        SpringApplication.run(JobFairApplication.class, args);
    }


}
