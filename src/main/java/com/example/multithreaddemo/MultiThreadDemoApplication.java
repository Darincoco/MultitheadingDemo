package com.example.multithreaddemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiThreadDemoApplication implements CommandLineRunner {

    @Autowired
    EventGenerator eventGenerator;
    @Autowired
    EventSenderImpl eventSender;

    public static void main(String[] args) {
        SpringApplication.run(MultiThreadDemoApplication.class, args);
    }

    @Override
    public void run (String... args) {
        eventGenerator.generateMessage();
        eventSender.sendEvents();
    }

}
