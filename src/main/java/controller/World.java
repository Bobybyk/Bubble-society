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

		Timer chrono = new Timer();
        chrono.schedule(new SpawnTimer(this), 0, 5000);
        chrono.schedule(new LifeTimer(this), 0, 10000);
    }

    public boolean decreaseHpForMap() {
        if (map.getNbrWorkers() == 0) {
            return false;
        }
        System.out.println("WORKERS LIFE");
        ArrayList<Worker> workersToErase = new ArrayList<Worker>();
        for (HashMap.Entry<Worker, Double[]> w : map.getMapList().entrySet()) {
            if (!w.getKey().getZone()) {
                w.getKey().decreaseHp();
            }
            if (w.getKey().getHp() < 1) {
                workersToErase.add(w.getKey());
            }
            System.out.println("    " + w.getKey().getHp());
        }
        for (Worker we : workersToErase) {
            map.removeWorker(we);
        }
        return true;
    }

    public void multiSpawn() {
        int nbr = new Random().nextInt(5) + 1;
        for (int i = 0 ; i < nbr ; i++) {
            spawnWorker();
        }
        System.out.println("    " + nbr + " : " + this.map.getNbrWorkers());
    }

    public void spawnWorker() {
        int hp = new Random().nextInt((50 - 25) + 1) + 25;
        int will = hp - (new Random().nextInt((hp - 20) + 1) + 20);
        double radius = new Random().nextInt(10) + 1;
        Worker worker = new WorkerBuilder().setHp(hp).setWill(will).setZone(false).setRadius(radius).buildFollower();
        this.map.addWorkerToMap(worker);
    }

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
