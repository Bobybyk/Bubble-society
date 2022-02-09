/*
 *
 * Copyright (c) 2022 Matthieu Le Franc
 *
 * You are prohibited from sharing and distributing this creation without our prior authorization, more specifically:
 * 
 * TO PROVIDE A COPY OF OUR GAME TO ANY THIRD PARTY;
 * TO USE THIS CREATION FOR COMMERCIAL PURPOSES;
 * TO USE THIS CREATIONS FOR PROFIT;
 * TO ALLOW ANY THIRD PARTY TO ACCESS TO THIS CREATION IN AN UNFAIR OR ABUSIVE MANNER;
 * 
 */
package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;

import application.debug.DebugLogger;
import game.model.GameMap;
import game.model.timer.LifeTimer;
import game.model.timer.ShiftTimer;
import game.model.timer.SpawnTimer;
import game.model.worker.Follower;
import game.model.worker.Insurgent;
import game.model.worker.Worker;
import game.model.worker.WorkerBuilder;


public class Game {
    private GameMap map;

    public Game() {
        this.map = new GameMap();

        /*
         * Time system declaration
         * (class timer line, start, period)
         */
		Timer chrono = new Timer();
        //chrono.schedule(new SpawnTimer(this), 0, 5000);
        //chrono.schedule(new LifeTimer(this), 0, 10000);
        chrono.schedule(new ShiftTimer(map), 0, 100);
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
        if (DebugLogger.debug) System.out.println("WORKERS LIFE");
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
            if (DebugLogger.debug) System.out.println("    " + w.getKey().getHp());
        }
        // delete every workers in workersToErase list in world list
        for (Worker we : workersToErase) {
            map.removeWorker(we);
        }
        return true;
    }

    /*
     * for each worker of the map, check if it encount an other worker
     */
    public void testMeeting() {
        if (map.getNbrWorkers() == 0) {
            return;
        }
        //DEBBUG
        if (DebugLogger.debug) System.out.println("WORKERS MEETING");
        for (HashMap.Entry<Worker, Double[]> w1 : map.getMapList().entrySet()) {
            for (HashMap.Entry<Worker, Double[]> w2 : map.getMapList().entrySet()) { 
                if (w1 != w2) {
                    HashMap<Worker, Worker> workersMet = map.workerMeeting(w1.getKey(), w2.getKey());
                    for (HashMap.Entry<Worker, Worker> worker : workersMet.entrySet()) {
                        if(worker.getKey().isInsurgent() && worker.getValue().isFollower()) {
                            if (DebugLogger.debug) System.out.println("    insurgent meets follower");
                            //attaque
                        }
                        if(worker.getKey().isInsurgent() && worker.getValue().isFollower()) {
                            if (DebugLogger.debug) System.out.println("    follower meets insurgent");
                            //fuite
                        }
                        if(worker.getKey().isInsurgent() && worker.getValue().isInsurgent()) {
                            if (DebugLogger.debug) System.out.println("    insurgent meets insurgent");
                            //échange
                        }
                        if(worker.getKey().isFollower() && worker.getValue().isFollower()) {
                            if (DebugLogger.debug) System.out.println("    follower meets follower");
                            //échange
                        }
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
        if (DebugLogger.debug) System.out.println("    " + nbr + " : " + this.map.getNbrWorkers());
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
