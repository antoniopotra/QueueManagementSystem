package org.example.model;

import org.example.GUI.Simulation;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private final int id;
    private boolean open = true;

    public Server(int capacity, int id) {
        this.tasks = new ArrayBlockingQueue<>(capacity);
        this.waitingPeriod = new AtomicInteger(0);
        this.id = id;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.getAndAdd(newTask.serviceTime());
    }

    @Override
    public void run() {
        while (open) {
            if (tasks.isEmpty()) continue;

            Task first = tasks.peek();
            try {
                Thread.sleep(first.serviceTime() * Constants.TIME_UNIT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            tasks.remove();
            waitingPeriod.getAndAdd(-first.serviceTime());
        }
    }

    public int size() {
        return tasks.size();
    }

    public boolean isFull() {
        return tasks.remainingCapacity() <= 0;
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public void close() {
        open = false;
    }

    public int getId() {
        return id;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }
}
