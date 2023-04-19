package org.example.businessLogic;

import org.example.GUI.Simulation;
import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer, Simulation simulation) {
        servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(maxTasksPerServer, i + 1 ,simulation);
            servers.add(server);

            Thread t = new Thread(server);
            t.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ShortestTimeStrategy();
        }
    }

    public int dispatchTask(Task task) {
        return strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }
}
