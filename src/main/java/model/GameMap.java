package model;

import java.util.HashMap;

public class GameMap {
    private HashMap<Worker, Double> workersToCenterRadius;

    public GameMap() {
        this.workersToCenterRadius = new HashMap<Worker, Double>();
    }

    public void removeWorker(Worker worker) {
        this.workersToCenterRadius.remove(worker);
    }
}
