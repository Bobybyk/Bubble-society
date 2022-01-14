package controller;

import java.util.Random;
import java.util.Timer;

import model.Follower;
import model.GameMap;
import model.Insurgent;
import model.Worker;
import model.WorkerBuilder;
import model.SpawnTimer;

public class World {
    private GameMap map;

    public World() {
        this.map = new GameMap();
        System.out.println(this.map.getNbrWorkers());
		Timer chrono = new Timer();
        chrono.schedule(new SpawnTimer(this), 0, 1000);
    }

    public void multiSpawn() {
        int nbr = new Random().nextInt(5) + 1;
        for (int i = 0 ; i < nbr ; i++) {
            spawnWorker();
        }
        System.out.println(nbr + " : " + this.map.getNbrWorkers());
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
            worker = null;
        } else if (worker instanceof Insurgent) {
            map.addWorkerToMap(new WorkerBuilder().setHp(worker.getHp()).setWill(worker.getWill()).setZone(worker.getZone()).setRadius(worker.getRadius()).buildFollower());
            map.removeWorker(worker);
            worker = null;
        }
    }
}
