package com.example.multithreaddemo;

public interface IEventSender {

    boolean sendToQueue(EventMessage message) throws InterruptedException;
}
