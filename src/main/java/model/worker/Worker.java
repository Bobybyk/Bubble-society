package model.worker;

public abstract class Worker {

    protected int hp; // [25 ; 50] état initial
    protected int will_power; // [10 ; 20] - état initial
    protected boolean zone;
    protected double radius;

    public Worker (WorkerBuilder worker) {
        this.hp = worker.getHp();
        this.will_power = worker.getWill();
        this.zone = worker.getZone();
        this.radius = worker.getRadius();
    }

    public int getHp() {
        return this.hp;
    }
    public int getWill() {
        return this.will_power;
    }
    public boolean getZone() {
        return this.zone;
    }
    public double getRadius() {
        return this.radius;
    }

    public void decreaseHp() {
        this.hp--;
    }
    public void increaseHp() {
        this.hp++;
    }

}
