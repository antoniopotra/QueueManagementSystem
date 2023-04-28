package org.example.model;

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
        if (tasks.isEmpty()) {
            newTask.incrementServiceTime();
        }
        tasks.add(newTask);
        waitingPeriod.getAndAdd(newTask.serviceTime());
    }

    @Override
    public synchronized void run() {
        while (open) {
            if (tasks.isEmpty()) continue;

            Task first = tasks.peek();
            while (first.serviceTime() > 0) {
                try {
                    Thread.sleep(Constants.TIME_UNIT);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                waitingPeriod.getAndAdd(-1);
                first.decrementServiceTime();
            }

            tasks.remove();
            if (tasks.isEmpty()) {
                waitingPeriod.set(0);
            }
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
