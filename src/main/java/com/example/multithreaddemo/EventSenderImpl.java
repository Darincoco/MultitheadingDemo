package com.example.multithreaddemo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Service
public class EventSenderImpl implements IEventSender{

    private LinkedBlockingDeque<EventMessage> queue = new LinkedBlockingDeque<>();

    @Autowired
    ThreadPoolTaskExecutor senderExecutor;

    @PreDestroy
    public void cleanup() {
        senderExecutor.shutdown();
    }

    @PostConstruct
    public void init() {
        int numberOfThreads = 5;
        List<Callable<Object>> tasks = Collections.nCopies(numberOfThreads, Executors.callable(this::sendEvents));
        try {
            senderExecutor.getThreadPoolExecutor().invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    @Override
    public boolean sendToQueue(EventMessage message) throws InterruptedException {
        return queue.offer(message, 10L, TimeUnit.SECONDS);
    }

    public void sendEvents() {
        senderExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    EventMessage message = queue.take();
                    System.out.println(Thread.currentThread().getName() + " send message: " + message.getMessageId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupt status and exit the loop
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        senderExecutor.shutdown();

    }
}
