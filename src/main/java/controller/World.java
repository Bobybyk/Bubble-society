package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

import model.GameMap;
import model.timer.LifeTimer;
import model.timer.SpawnTimer;
import model.worker.Follower;
import model.worker.Insurgent;
import model.worker.Worker;
import model.worker.WorkerBuilder;

public class World {
    private GameMap map;

    public World() {
        this.map = new GameMap();

        /*
         * Time system declaration
         * (class timer line, start, period)
         */
		Timer chrono = new Timer();
        chrono.schedule(new SpawnTimer(this), 0, 5000);
        chrono.schedule(new LifeTimer(this), 0, 10000);
    }

    /*
     * decrease hp manager
     * return true if workers exist, else false
     */
    public boolean decreaseHpForMap() {
        if (map.getNbrWorkers() == 0) {
            return false;
        }
        // DEBBUG
        System.out.println("WORKERS LIFE");
        ArrayList<Worker> workersToErase = new ArrayList<Worker>();
        for (HashMap.Entry<Worker, Double[]> w : map.getMapList().entrySet()) {
            // if worker is not in a zone, decrease hp
            if (!w.getKey().getZone()) {
                w.getKey().decreaseHp();
            }
            // if worker has 0 hp, add it to the erase list
            if (w.getKey().getHp() < 1) {
                workersToErase.add(w.getKey());
            }
            // DEBBUG
            System.out.println("    " + w.getKey().getHp());
        }
        // delete every workers in workersToErase list in world list
        for (Worker we : workersToErase) {
            map.removeWorker(we);
        }
        return true;
    }

    public void testMeeting() {
        if (map.getNbrWorkers() == 0) {
            return;
        }
        //DEBBUG
        System.out.println("WORKERS MEETING");
        for (HashMap.Entry<Worker, Double[]> w1 : map.getMapList().entrySet()) {
            for (HashMap.Entry<Worker, Double[]> w2 : map.getMapList().entrySet()) { 
                if (w1 != w2) {
                    ArrayList<Worker> workersMet = map.workerMeeting(w1.getKey(), w2.getKey());
                    for (Worker worker : workersMet) {
                        System.out.println(worker.getRadius() + " met a worker");
                    }
                }
            }
        }
    }

    /*
     * spawn random number of workers (1 to 5)
     */
    public void multiSpawn() {
        int nbr = new Random().nextInt(5) + 1;
        for (int i = 0 ; i < nbr ; i++) {
            spawnWorker();
        }
        // DEBBUG
        System.out.println("    " + nbr + " : " + this.map.getNbrWorkers());
    }

    /*
     * spawn and init a follower
     */
    public void spawnWorker() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = hp - (new Random().nextInt((hp - 20) + 1) + 20);
        double radius = new Random().nextInt(10) + 1;
        Worker worker = new WorkerBuilder().setHp(hp).setWill(will).setZone(false).setRadius(radius).buildFollower();
        this.map.addWorkerToMap(worker);
    }

    /*
     * change worker state
     * follower to insurgent ; insurgent to follower
     */
    public void changeWorkerState(Worker worker) {
        if (worker instanceof Follower) {
            map.addWorkerToMap(new WorkerBuilder().setHp(worker.getHp()).setWill(worker.getWill()).setZone(worker.getZone()).setRadius(worker.getRadius()).buildInsurgent());
            map.removeWorker(worker);
        } else if (worker instanceof Insurgent) {
            map.addWorkerToMap(new WorkerBuilder().setHp(worker.getHp()).setWill(worker.getWill()).setZone(worker.getZone()).setRadius(worker.getRadius()).buildFollower());
            map.removeWorker(worker);
        }
    }
}
