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

    /** decrease hp manager return true if workers exist, else false */
    public void decreaseWorkersHp() {
        if (getNbrWorkers() == 0) {
            return;
        }

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

            DebugLogger.print(DebugType.ENTITIES, "" + w.getValue().getHp());
        }
    }

    /** spawn random number of workers (1 to 5) */
    public void multiSpawn() {
        int nbr = new Random().nextInt(5) + 1;
        for (int i = 0; i < nbr; i++) {
            spawnWorker(1);
        }

        DebugLogger.print(DebugType.ENTITIES, nbr + " : " + getNbrWorkers());
    }

    /** spawn and init a follower */
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

    /**
     * build and return a new insurgent
     *
     * @param hp max hp
     * @param will max will
     * @param radius radius of the insurgent
     * @return new insurgent
     */
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

    /**
     * build and return a new insurgent with random stats
     *
     * @return new insurgent
     */
    private Insurgent buildRandomInsurgent() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = new Random().nextInt(hp) + 5;
        while (will > hp) will--;
        double radius = new Random().nextInt(10) + 1;
        return buildInsurgent(hp, will, radius);
    }

    /**
     * build and return a new follower
     *
     * @param hp max hp
     * @param will max will
     * @param radius radius of the follower
     * @return new follower
     */
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

    /**
     * build and return a new follower with random stats
     *
     * @return new follower
     */
    private Follower buildRandomFollower() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = new Random().nextInt(hp) + 5;
        while (will > hp) will--;
        double radius = new Random().nextInt(10) + 1;
        return buildFollower(hp, will, radius);
    }

    /**
     * convert follower to insurgent
     *
     * @param worker follower
     * @return insurgent
     */
    private Insurgent followerToInsurgent(Follower worker) {
        return (Insurgent)
                new WorkerBuilder()
                        .setHp(worker.getHp())
                        .setWill(worker.getWill())
                        .setZone(worker.getZone())
                        .setRadius(worker.getRadius())
                        .buildInsurgent();
    }

    /**
     * convert insurgent to follower
     *
     * @param worker insurgent
     * @return follower
     */
    private Follower insurgentToFollower(Follower worker) {
        return (Follower)
                new WorkerBuilder()
                        .setHp(worker.getHp())
                        .setWill(worker.getWill())
                        .setZone(worker.getZone())
                        .setRadius(worker.getRadius())
                        .buildFollower();
    }

    /**
     * define entity without graphical part
     *
     * @param wd entity
     */
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

    /**
     * change wander mode of all workers
     *
     * @param state new state
     */
    public void changeWanderMode(boolean state) {
        for (HashMap.Entry<Entity, Worker> entity : workerBindView.entrySet()) {
            entity.getValue().setWanderState(state);
        }
    }

    /**
     * @return number of workers
     */
    private int getNbrWorkers() {
        return workerBindView.size();
    }

    /**
     * @return Entities binded to view
     */
    public HashMap<Entity, Worker> getWorkerBindView() {
        return workerBindView;
    }

    /**
     * remove entity from binded view
     *
     * @param entity entity to remove
     * @return worker binded to entity
     */
    public Worker removeEntity(Entity entity) {
        Worker tmp = workerBindView.get(entity);
        workerBindView.remove(entity);
        return tmp;
    }

    /**
     * change worker state
     *
     * @param worker worker
     * @param entity entity
     */
    public void changeWorkerState(Worker worker, Entity entity) {
        if (worker instanceof Follower) {
            workerBindView.put(entity, followerToInsurgent((Follower) worker));
        } else if (worker instanceof Insurgent) {
            workerBindView.put(entity, insurgentToFollower((Follower) worker));
        }
    }
}
