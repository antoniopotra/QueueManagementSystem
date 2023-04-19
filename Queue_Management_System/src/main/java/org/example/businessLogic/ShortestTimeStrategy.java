package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestTimeStrategy implements Strategy {
    @Override
    public int addTask(List<Server> servers, Task task) {
        Server fastest = servers.get(0);
        int index = 0;

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            if (!server.isFull() && server.getWaitingPeriod() < fastest.getWaitingPeriod()) {
                fastest = server;
                index = i;
            }
        }
        fastest.addTask(task);

        return index + 1;
    }
}
