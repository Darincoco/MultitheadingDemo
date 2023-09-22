package com.example.multithreaddemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class EventGenerator {

    @Autowired
    EventSenderImpl eventSender;
    @Autowired
    ThreadPoolTaskExecutor generatorExecutor;


    public void generateMessage() {
        int totalMessage = 100;
        generatorExecutor.submit( () -> {
            for (int i = 0; i < totalMessage; i++) {
                EventMessage message = new EventMessage();
                message.setMessageId(i);
                message.setContent("Content");

                try {
                    eventSender.sendToQueue(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("%s sent message %s to queue \n", Thread.currentThread().getName(), message.getMessageId());
            }
        });


    }

}
