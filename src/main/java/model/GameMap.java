package model;

import java.util.HashMap;
import java.util.Random;

public class GameMap {
    private HashMap<Worker, Double> workersToCenterRadius;

    public GameMap() {
        this.workersToCenterRadius = new HashMap<Worker, Double>();
    }

    public void removeWorker(Worker worker) {
        this.workersToCenterRadius.remove(worker);
    }

    public void addWorkerToMap(Worker worker) {
        double radius = new Random().nextInt(10) + 1;
        workersToCenterRadius.putIfAbsent(worker, radius);
    }
}
