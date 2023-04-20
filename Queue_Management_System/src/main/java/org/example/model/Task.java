package org.example.model;

public record Task(int id, int arrivalTime, int serviceTime) {
    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", id, arrivalTime, serviceTime);
    }
}
