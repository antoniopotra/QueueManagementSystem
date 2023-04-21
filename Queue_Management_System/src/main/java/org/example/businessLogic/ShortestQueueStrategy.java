package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public int addTask(List<Server> servers, Task task) {
        Server shortest = servers.get(0);

        for (Server server : servers) {
            if (!server.isFull() && server.size() < shortest.size()) {
                shortest = server;
            }
        }
        shortest.addTask(task);

        return shortest.getWaitingPeriod();
    }
}
