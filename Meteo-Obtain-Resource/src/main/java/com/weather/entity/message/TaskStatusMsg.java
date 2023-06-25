package com.weather.entity.message;

import lombok.Data;

@Data
public class TaskStatusMsg {
    private String task;

    public TaskStatusMsg(String task) {
        this.task = task;
    }
    public TaskStatusMsg(){}
}
