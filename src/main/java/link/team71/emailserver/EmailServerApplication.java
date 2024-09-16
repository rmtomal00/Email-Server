package link.team71.emailserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class EmailServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailServerApplication.class, args);
    }

}
