package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    @Override
    public int addTask(List<Server> servers, Task task) {
        Server shortest = servers.get(0);
        int index = 0;

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            if (!server.isFull() && server.size() < shortest.size()) {
                shortest = server;
                index = i;
            }
        }
        shortest.addTask(task);

        return index + 1;
    }
}
