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

import application.debug.DebugLogger;
import application.debug.DebugType;
import entity.Entity;
import entity.FollowerDisplay;
import entity.InsurgentDisplay;
import game.worker.Follower;
import game.worker.Insurgent;
import game.worker.Worker;
import game.worker.WorkerBuilder;
import java.util.HashMap;
import java.util.Random;
import world.World;

public class Game {
    private World gWorld;
    private HashMap<Entity, Worker> workerBindView;

    public Game(World gWorld) {
        this.gWorld = gWorld;
        this.workerBindView = new HashMap<Entity, Worker>();
    }

    /*
     * decrease hp manager
     * return true if workers exist, else false
     */
    public void decreaseWorkersHp() {
        if (getNbrWorkers() == 0) {
            return;
        }
        // DEBBUG
        DebugLogger.print(DebugType.ENTITIES, "WORKERS LIFE");
        for (HashMap.Entry<Entity, Worker> w : workerBindView.entrySet()) {
            // if worker is not in a zone, decrease hp
            if (!gWorld.entityIsInZone(w.getKey(), this) && w.getValue().getLifeState()) {
                w.getValue().decreaseHp();
            }
            // if worker has 0 hp, add it to the erase list
            if (w.getValue().getHp() < 1 && w.getValue().getLifeState()) {
                gWorld.killEntity(w.getKey());
                w.getValue().setLifeState(false);
            }
            // DEBBUG
            DebugLogger.print(DebugType.ENTITIES, "" + w.getValue().getHp());
        }
    }

    /*
     * spawn random number of workers (1 to 5)
     */
    public void multiSpawn() {
        int nbr = new Random().nextInt(5) + 1;
        for (int i = 0; i < nbr; i++) {
            spawnWorker(1);
        }
        // DEBBUG
        DebugLogger.print(DebugType.ENTITIES, nbr + " : " + getNbrWorkers());
    }

    /*
     * spawn and init a follower
     */
    public void spawnWorker(int id) {

        Worker worker = null;
        Entity wd = null;

        switch (id) {
            case 1:
                worker = buildRandomFollower();
                wd = gWorld.spawnEntity(1);
                workerBindView.put(wd, worker);
                wd.setWorker(worker);
                break;
            case 2:
                worker = buildRandomInsurgent();
                wd = gWorld.spawnEntity(2);
                workerBindView.put(wd, worker);
                wd.setWorker(worker);
                break;
        }
    }

    // ---------------

    private Insurgent buildInsurgent(int hp, int will, double radius) {
        return (Insurgent)
                new WorkerBuilder()
                        .setHp(hp)
                        .setWill(will)
                        .setZone(false)
                        .setRadius(radius)
                        .setWanderState(true)
                        .setLifeState(true)
                        .buildInsurgent();
    }

    private Insurgent buildRandomInsurgent() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = new Random().nextInt(hp) + 5;
        while (will > hp) will--;
        double radius = new Random().nextInt(10) + 1;
        return buildInsurgent(hp, will, radius);
    }

    private Follower buildFollower(int hp, int will, double radius) {
        return (Follower)
                new WorkerBuilder()
                        .setHp(hp)
                        .setWill(will)
                        .setZone(false)
                        .setRadius(radius)
                        .setWanderState(true)
                        .setLifeState(true)
                        .buildFollower();
    }

    private Follower buildRandomFollower() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = new Random().nextInt(hp) + 5;
        while (will > hp) will--;
        double radius = new Random().nextInt(10) + 1;
        return buildFollower(hp, will, radius);
    }

    private Insurgent followerToInsurgent(Follower worker) {
        return (Insurgent)
                new WorkerBuilder()
                        .setHp(worker.getHp())
                        .setWill(worker.getWill())
                        .setZone(worker.getZone())
                        .setRadius(worker.getRadius())
                        .buildInsurgent();
    }

    private Follower insurgentToFollower(Follower worker) {
        return (Follower)
                new WorkerBuilder()
                        .setHp(worker.getHp())
                        .setWill(worker.getWill())
                        .setZone(worker.getZone())
                        .setRadius(worker.getRadius())
                        .buildFollower();
    }

    // ---------------

    // to define entity without graphical part
    public void defineEntity(Entity wd) {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = hp - (new Random().nextInt((hp - 20) + 1) + 20);
        double radius = new Random().nextInt(10) + 1;
        Worker worker = null;
        if (wd instanceof FollowerDisplay) {
            worker = buildFollower(hp, will, radius);
        }
        if (wd instanceof InsurgentDisplay) {
            worker = buildInsurgent(hp, will, radius);
        }
        workerBindView.put(wd, worker);
        wd.setWorker(worker);
    }

    public void changeWanderMode(boolean state) {
        for (HashMap.Entry<Entity, Worker> entity : workerBindView.entrySet()) {
            entity.getValue().setWanderState(state);
        }
    }

    private int getNbrWorkers() {
        return workerBindView.size();
    }

    public HashMap<Entity, Worker> getWorkerBindView() {
        return workerBindView;
    }

    public Worker removeEntity(Entity entity) {
        Worker tmp = workerBindView.get(entity);
        workerBindView.remove(entity);
        return tmp;
    }

    public void changeWorkerState(Worker worker, Entity entity) {
        if (worker instanceof Follower) {
            workerBindView.put(entity, followerToInsurgent((Follower) worker));
        } else if (worker instanceof Insurgent) {
            workerBindView.put(entity, insurgentToFollower((Follower) worker));
        }
    }
}
