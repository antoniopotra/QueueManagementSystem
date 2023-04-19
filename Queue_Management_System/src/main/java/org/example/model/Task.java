package org.example.model;

public class Task {
    private final int id;
    private final int arrivalTime;
    private final int serviceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", id, arrivalTime, serviceTime);
    }
}
