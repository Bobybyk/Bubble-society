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
import java.util.function.DoubleBinaryOperator;

import application.debug.DebugLogger;
import application.debug.DebugType;
import entity.Entity;
import entity.WorkerDisplay;
import game.model.worker.Follower;
import game.model.worker.Insurgent;
import game.model.worker.Worker;
import game.model.worker.WorkerBuilder;
import world.World;


public class Game {
    private World gWorld;
    private HashMap<Worker, WorkerDisplay> workerBindView = new HashMap<Worker, WorkerDisplay>();

    public Game(World gWorld) {
        
        this.gWorld = gWorld;
    }

    /*
     * decrease hp manager
     * return true if workers exist, else false
     */
    /* TO REFACTOR 
    public boolean decreaseHpForMap() {
        if (map.getNbrWorkers() == 0) {
            return false;
        }
        // DEBBUG
        DebugLogger.print(DebugType.ENTITIES, "WORKERS LIFE");
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
            DebugLogger.print(DebugType.ENTITIES, ""+w.getKey().getHp());
        }
        // delete every workers in workersToErase list in world list
        for (Worker we : workersToErase) {
            map.removeWorker(we);
        }
        return true;
    }
    */

    /*
     * for each worker of the map, check if it encount an other worker
     */
    /* TO REFACTOR 
    public void testMeeting() {
        if (map.getNbrWorkers() == 0) {
            return;
        }
        //DEBBUG
        DebugLogger.print(DebugType.ALL, "WORKERS MEETING");
        for (HashMap.Entry<Worker, Double[]> w1 : map.getMapList().entrySet()) {
            for (HashMap.Entry<Worker, Double[]> w2 : map.getMapList().entrySet()) { 
                if (w1 != w2) {
                    HashMap<Worker, Worker> workersMet = map.workerMeeting(w1.getKey(), w2.getKey());
                    for (HashMap.Entry<Worker, Worker> worker : workersMet.entrySet()) {
                        if(worker.getKey().isInsurgent() && worker.getValue().isFollower()) {
                            DebugLogger.print(DebugType.ENTITIES, "insurgent meets follower");
                            //attaque
                        }
                        if(worker.getKey().isInsurgent() && worker.getValue().isFollower()) {
                            DebugLogger.print(DebugType.ENTITIES, "follower meets insurgent");
                            //fuite
                        }
                        if(worker.getKey().isInsurgent() && worker.getValue().isInsurgent()) {
                            DebugLogger.print(DebugType.ENTITIES, "insurgent meets insurgent");
                            //échange
                        }
                        if(worker.getKey().isFollower() && worker.getValue().isFollower()) {
                            DebugLogger.print(DebugType.ENTITIES, "follower meets follower");
                            //échange
                        }
                    }
                }
            }
        }
    } */

    /*
     * spawn random number of workers (1 to 5)
     */
    public void multiSpawn() {
        int nbr = new Random().nextInt(5) + 1;
        for (int i = 0 ; i < nbr ; i++) {
            spawnWorker();
        }
        // DEBBUG
        DebugLogger.print(DebugType.ENTITIES, nbr + " : " + getNbrWorkers());
    }

    /*
     * spawn and init a follower
     */
    public void spawnWorker() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = hp - (new Random().nextInt((hp - 20) + 1) + 20);
        double radius = new Random().nextInt(10) + 1;
        Worker worker = new WorkerBuilder().setHp(hp).setWill(will).setZone(false).setRadius(radius).buildFollower();
        WorkerDisplay wd = gWorld.spawnWorker();
        workerBindView.put(worker, wd);
    }

    private int getNbrWorkers() {
        return workerBindView.size();
    }

    /*
     * change worker state
     * follower to insurgent ; insurgent to follower
     */
    /* TO REFACTOR 
    public void changeWorkerState(Worker worker) {
        if (worker instanceof Follower) {
            map.addWorkerToMap(new WorkerBuilder().setHp(worker.getHp()).setWill(worker.getWill()).setZone(worker.getZone()).setRadius(worker.getRadius()).buildInsurgent());
            map.removeWorker(worker);
        } else if (worker instanceof Insurgent) {
            map.addWorkerToMap(new WorkerBuilder().setHp(worker.getHp()).setWill(worker.getWill()).setZone(worker.getZone()).setRadius(worker.getRadius()).buildFollower());
            map.removeWorker(worker);
        }
    } */
}
