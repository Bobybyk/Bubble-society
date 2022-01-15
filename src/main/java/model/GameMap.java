package model;

import java.util.HashMap;
import java.util.Random;

import model.worker.Worker;

public class GameMap {
    private HashMap<Worker, Double[]> workersToCenterRadius;

    public GameMap() {
        this.workersToCenterRadius = new HashMap<Worker, Double[]>();
    }

    /*
     * erase worker from world list and delete object
     */
    public void removeWorker(Worker worker) {
        this.workersToCenterRadius.remove(worker);
        worker = null;
    }

    /*
     * put worker on map with random radius to center of map (1 to 10)
     */
    public void addWorkerToMap(Worker worker) {
        double coordinateX = new Random().nextInt(10) + 1;
        //define sign for X dimension (plus or minus)
        int sign = new Random().nextInt(2);
        if (sign == 0) {
            coordinateX *= -1;
        }
        double coordinateY = new Random().nextInt(10) + 1;
        //define sign for Y dimension (plus or minus)
        sign = new Random().nextInt(2);
        if (sign == 0) {
            coordinateY *= -1;
        }
        // DEBBUG
        System.out.println("    radius : " + coordinateX + " ; " + coordinateY);
        Double[] coordinatesCouple = {coordinateX, coordinateY};
        workersToCenterRadius.putIfAbsent(worker, coordinatesCouple);
    }

    /*
     * return world workers list size
     */
    public int getNbrWorkers() {
        return workersToCenterRadius.size();
    }

    public HashMap<Worker, Double[]> getMapList() {
        return workersToCenterRadius;
    }
}
