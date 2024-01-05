package com.taskssystem.config.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "tasks-system",
            groupId = "groupId")
    void listener(String data) {
        System.out.println("Listener receive: " + data);

    }
}
