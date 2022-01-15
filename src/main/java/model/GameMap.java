package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;

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

    public ArrayList<Worker> workerMeeting(Worker w1, Worker w2) {
        double x = Math.abs(workersToCenterRadius.get(w2)[0]-workersToCenterRadius.get(w1)[0]);
        x *= x;
        double y = Math.abs(workersToCenterRadius.get(w2)[1]-workersToCenterRadius.get(w1)[1]);
        y *= y;
        ArrayList<Worker> ret = new ArrayList<Worker>();
        if (Math.sqrt(x+y) < w1.getRadius()) {
            ret.add(w1);
        } 
        if (Math.sqrt(x+y) > w1.getRadius()) {
            ret.add(w2);
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
