package org.example.model;

public final class Task {
    private final int id;
    private final int arrivalTime;
    private int serviceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", id, arrivalTime, serviceTime);
    }

    public int id() {
        return id;
    }

    public int arrivalTime() {
        return arrivalTime;
    }

    public int serviceTime() {
        return serviceTime;
    }

    public void decrementServiceTime() {
        serviceTime--;
    }

    public void incrementServiceTime() {
        serviceTime++;
    }
}
