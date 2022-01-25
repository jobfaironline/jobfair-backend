package org.capstone.job_fair;

import org.capstone.job_fair.utils.NetworkUtil;
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
        try{
            System.out.print("Leu leu: ");
            NetworkUtil.sendPingRequest("8.8.8.8");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.print(System.getenv("DATASOURCE_PASSWORD"));
        SpringApplication.run(JobFairApplication.class, args);
    }


}
