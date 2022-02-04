package org.capstone.job_fair;

import org.capstone.job_fair.services.interfaces.util.AWSMailService;
import org.capstone.job_fair.utils.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


@SpringBootApplication
@RestController
@Import(SpringDataRestConfiguration.class)
@EnableAsync
public class JobFairApplication {
    @Value("spring.mail.username")
    private static String mailUser;


    public static void main(String[] args) {
        SpringApplication.run(JobFairApplication.class, args);
    }


}
