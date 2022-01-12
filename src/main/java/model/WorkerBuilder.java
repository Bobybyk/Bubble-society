package model;

public class WorkerBuilder {

    private int hp; // [25 ; 50] état initial
    private int will_power; // [10 ; 20] - état initial

    public WorkerBuilder setHp(int hp) {
        this.hp = hp;
        return this;
    }

    public WorkerBuilder setWill(int will_power) {
        this.will_power = will_power;
        return this;
    }

    public Worker buildFollower() {
        return new Follower(this);
    }
    public Worker buildInsurgent() {
        return new Insurgent(this);
    }

    public int getHp() {
        return this.hp;
    }
    public int getWill() {
        return this.will_power;
    }
}