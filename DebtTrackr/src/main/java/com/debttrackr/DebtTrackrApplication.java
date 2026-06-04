package com.debttrackr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching

public class DebtTrackrApplication {

    public static void main(String[] args) {
        System.out.println("Hello suresh");
        SpringApplication.run(DebtTrackrApplication.class, args);
    }

}
