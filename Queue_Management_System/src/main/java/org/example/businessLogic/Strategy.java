package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public interface Strategy {
    int addTask(List<Server> servers, Task task);
}
