package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestTimeStrategy implements Strategy {
    @Override
    public int addTask(List<Server> servers, Task task) {
        Server fastest = servers.get(0);

        for (Server server : servers) {
            if (!server.isFull() && server.getWaitingPeriod() < fastest.getWaitingPeriod()) {
                fastest = server;
            }
        }

        fastest.addTask(task);

        return fastest.getWaitingPeriod();
    }
}
