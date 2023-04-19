package org.example;

import org.example.businessLogic.SimulationManager;

public class Main {
    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        gen.init();

        Thread t = new Thread(gen);
        t.start();
    }
}