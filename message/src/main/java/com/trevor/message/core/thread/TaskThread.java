package com.trevor.message.core.thread;


import com.trevor.message.bo.Task;
import com.trevor.message.core.Core;

public class TaskThread implements Runnable{

    public Task task;

    public static Core core;

    public TaskThread(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        core.execut(task);
    }
}
