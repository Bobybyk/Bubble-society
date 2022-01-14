package model;

import java.util.HashMap;
import java.util.Random;

public class GameMap {
    private HashMap<Worker, Double[]> workersToCenterRadius;

    public GameMap() {
        this.workersToCenterRadius = new HashMap<Worker, Double[]>();
    }

    public void removeWorker(Worker worker) {
        this.workersToCenterRadius.remove(worker);
    }

    public void addWorkerToMap(Worker worker) {
        double radiusX = new Random().nextInt(10) + 1;
        int sign = new Random().nextInt(2);
        if (sign == 0) {
            radiusX *= -1;
        }
        double radiusY = new Random().nextInt(10) + 1;
        sign = new Random().nextInt(2);
        if (sign == 0) {
            radiusY *= -1;
        }
        System.out.println("radius : " + radiusX + " ; " + radiusY);
        Double[] radiusCouple = {radiusX, radiusY};
        workersToCenterRadius.putIfAbsent(worker, radiusCouple);
    }

    public int getNbrWorkers() {
        return workersToCenterRadius.size();
    }
}
