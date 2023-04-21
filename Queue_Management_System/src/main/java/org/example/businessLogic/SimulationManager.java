package org.example.businessLogic;

import org.example.GUI.Simulation;
import org.example.GUI.StartMenu;
import org.example.model.Constants;
import org.example.model.Server;
import org.example.model.Task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable {
    private int noClients;
    private int noServers;
    private int timeLimit;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;
    private final List<Task> generatedTasks;
    private Scheduler scheduler;
    private final StartMenu startMenu;
    private Simulation simulation;
    private volatile boolean canStart = false;

    public SimulationManager() {
        startMenu = new StartMenu();
        generatedTasks = new ArrayList<>();

        startMenu.addSimulationListener(new SimulationListener());
    }

    public void init() {
        while (!canStart) Thread.onSpinWait();
        scheduler = new Scheduler(noServers, noClients);
        scheduler.changeStrategy(SelectionPolicy.SHORTEST_TIME);
    }

    @Override
    public void run() {
        FileWriter output = generateExecutionLog();

        float averageServiceTime = 0;
        for (Task task : generatedTasks) {
            averageServiceTime += task.serviceTime();
        }
        averageServiceTime /= (float) generatedTasks.size();

        int peakHour = -1, maxCount = -1;
        float averageWaitingTime = 0;
        int currentTime = 0;
        while (currentTime < timeLimit) {
            updateExecutionLog(output, currentTime);
            updateGui(currentTime);

            averageWaitingTime += dispatchTasks(currentTime);
            currentTime++;

            int count = 0;
            for (Server server : scheduler.getServers()) {
                count += server.size();
            }
            if (count > maxCount) {
                maxCount = count;
                peakHour = currentTime;
            }

            try {
                Thread.sleep(Constants.TIME_UNIT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        closeServers();
        averageWaitingTime /= (float) noClients;
        simulationResults(peakHour, averageServiceTime, averageWaitingTime);

        try {
            output.close();
        } catch (IOException ignored) {

        }
    }

    private void simulationResults(int peakHour, float averageServiceTime, float averageWaitingTime) {
        simulation.finish();
        simulation.showInfoPopup("Peak Hour: " + peakHour + "\nAverage Service Time: " + averageServiceTime + "\nAverage Waiting Time: " + averageWaitingTime);
    }

    private void closeServers() {
        for (Server server : scheduler.getServers()) {
            server.close();
        }
    }

    private int dispatchTasks(int currentTime) {
        Iterator<Task> i = generatedTasks.iterator();
        int waitingTime = 0;
        while (i.hasNext()) {
            Task task = i.next();
            if (task.arrivalTime() != currentTime + 1) continue;

            waitingTime += scheduler.dispatchTask(task);
            i.remove();
        }
        return waitingTime;
    }

    private void generateRandomTasks() {
        for (int i = 0; i < noClients; i++) {
            int id = i + 1;
            int arrivalTime = (int) Math.floor(Math.random() * (maxArrivalTime - minArrivalTime + 1) + minArrivalTime);
            int serviceTime = (int) Math.floor(Math.random() * (maxServiceTime - minServiceTime + 1) + minServiceTime);

            generatedTasks.add(new Task(id, arrivalTime, serviceTime));
        }
        generatedTasks.sort(Comparator.comparing(Task::arrivalTime));
    }

    private FileWriter generateExecutionLog() {
        FileWriter output = null;

        try {
            output = new FileWriter(String.format("log-%d-%d-%d-%d-%d-%d-%d.txt", noClients, noServers, timeLimit, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime));
            output.write("");
        } catch (IOException e) {
            simulation.showErrorPopup("The log of events was not created.");
        }

        return output;
    }

    private void updateExecutionLog(FileWriter output, int time) {
        if (output == null) return;

        try {
            output.append("Time ").append(String.valueOf(time)).append("\n").append("Waiting clients: ");

            for (Task task : generatedTasks) {
                output.append(task.toString()).append("; ");
            }
            output.append("\n");

            for (Server server : scheduler.getServers()) {
                output.append("Queue ").append(String.valueOf(server.getId())).append(": ");

                if (server.getTasks().isEmpty()) {
                    output.append("closed\n");
                    continue;
                }

                for (Task task : server.getTasks()) {
                    output.append(task.toString()).append("; ");
                }

                output.append("\n");
            }

            output.append("\n");
        } catch (IOException ignored) {

        }
    }

    private void updateGui(int currentTime) {
        StringBuilder update = new StringBuilder("Time " + currentTime + "\nWaiting clients: ");
        for (Task task : generatedTasks) {
            update.append(task.toString());
            update.append("; ");
        }
        update.append("\n");
        for (Server server : scheduler.getServers()) {
            update.append(String.format("Queue %d: ", server.getId()));
            if (server.getTasks().isEmpty()) {
                update.append("closed\n");
                continue;
            }
            for (Task task : server.getTasks()) {
                update.append(task.toString());
                update.append("; ");
            }
            update.append("\n");
        }
        simulation.updateLog(String.valueOf(update));
    }

    private class SimulationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getUserInput();

                simulation = new Simulation();
                simulation.start();

                generateRandomTasks();

                canStart = true;
            } catch (NumberFormatException exception) {
                startMenu.showErrorPopup("Only positive integers allowed.");
            }
        }

        private void getUserInput() throws NumberFormatException {
            noClients = Integer.parseInt(startMenu.clients());
            noServers = Integer.parseInt(startMenu.queues());
            timeLimit = Integer.parseInt(startMenu.simulationTime());
            minArrivalTime = Integer.parseInt(startMenu.minArrivalTime());
            maxArrivalTime = Integer.parseInt(startMenu.maxArrivalTime());
            minServiceTime = Integer.parseInt(startMenu.minServiceTime());
            maxServiceTime = Integer.parseInt(startMenu.maxServiceTime());

            if (noClients < 0 || noServers < 0 || timeLimit < 0 || minArrivalTime < 0 || maxArrivalTime < 0 || minServiceTime < 0 || maxServiceTime < 0) {
                throw new NumberFormatException();
            }

            if (minArrivalTime > maxArrivalTime) {
                int aux = minArrivalTime;
                minArrivalTime = maxArrivalTime;
                maxArrivalTime = aux;
            }
            if (minServiceTime > maxServiceTime) {
                int aux = minServiceTime;
                minServiceTime = maxServiceTime;
                maxServiceTime = aux;
            }
        }
    }
}
