package org.example.springaitooltest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.springaitooltest", "tooltest"})
public class SpringAiToolTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiToolTestApplication.class, args);
    }

}
