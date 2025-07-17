package com.playground.mscreditAnalizer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsCreditAnalizerApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(MsCreditAnalizerApplication.class, args);
    }

}
