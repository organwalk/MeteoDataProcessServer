package com.weather.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.entity.message.TaskStatusMsg;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskStatusListener {
    private boolean isTaskOver = false;

    @SneakyThrows
    @RabbitListener(queues = "task-over-queue")
    public void handleMessage(String jsonMsg) {
        TaskStatusMsg msg = new ObjectMapper().readValue(jsonMsg, TaskStatusMsg.class);
        log.info("now " + msg + " over");
        isTaskOver = true;
    }
    public boolean isTaskOver() {
        return isTaskOver;
    }
}
