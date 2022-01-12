package model;

public abstract class Worker {

    protected int hp; // [25 ; 50] état initial
    protected int will_power; // [10 ; 20] - état initial

    public Worker (WorkerBuilder worker) {
        this.hp = worker.getHp();
        this.will_power = worker.getWill();
    }

}
