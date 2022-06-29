package com.prgms.kokoahairshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KokoaHairshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(KokoaHairshopApplication.class, args);
    }

}
