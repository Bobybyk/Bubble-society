package game.model;

import java.util.HashMap;
import java.util.Random;

import application.debug.DebugLogger;
import game.model.worker.Worker;

import java.lang.Math;

public class GameMap {
    private HashMap<Worker, Double[]> workersToCenterRadius;
    private Pathfinding pathfindingManager;

    public GameMap() {
        this.workersToCenterRadius = new HashMap<Worker, Double[]>();
        this.pathfindingManager = new Pathfinding();
    }

    /*
     * erase worker from world list and delete object
     */
    public void removeWorker(Worker worker) {
        this.workersToCenterRadius.remove(worker);
        worker = null;
    }

    /*
     * check for two workers if their's a meeting
     * and then add them to the list (first the one which detects)
     */
    public HashMap<Worker, Worker> workerMeeting(Worker w1, Worker w2) {
        double x = Math.abs(workersToCenterRadius.get(w2)[0]-workersToCenterRadius.get(w1)[0]);
        x *= x;
        double y = Math.abs(workersToCenterRadius.get(w2)[1]-workersToCenterRadius.get(w1)[1]);
        y *= y;
        HashMap<Worker, Worker> ret = new HashMap<Worker, Worker>();
        if (Math.sqrt(x+y) < w1.getRadius()) {
            ret.put(w1, w2);
        } 
        if (Math.sqrt(x+y) > w1.getRadius()) {
            ret.put(w2, w1);
        }
        return ret;
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
        if (DebugLogger.debug) System.out.println("    radius : " + coordinateX + " ; " + coordinateY);
        Double[] coordinatesCouple = {coordinateX, coordinateY};
        workersToCenterRadius.putIfAbsent(worker, coordinatesCouple);
    }

    public void wander() {
        if (getNbrWorkers() == 0) {
            return;
        }
        // for each worker coordinates wandering : wander
        for (HashMap.Entry<Worker, Double[]> worker : workersToCenterRadius.entrySet()) {
            if (worker.getKey().getWanderState()) {
                workersToCenterRadius.put(worker.getKey(), pathfindingManager.wander(worker.getValue()));
            }
        } 
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
