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
package game.model.worker;

public class WorkerBuilder {

    private int hp; // [25 ; 50] état initial
    private int will_power; // [10 ; 20] - état initial
    private boolean zone;
    private double radius;
    private int speed;
    private int dps;
    private boolean wanderState;

    public WorkerBuilder setHp(int hp) {
        this.hp = hp;
        return this;
    }
    public WorkerBuilder setWill(int will_power) {
        this.will_power = will_power;
        return this;
    }
    public WorkerBuilder setZone(boolean zone) {
        this.zone = zone;
        return this;
    }
    public WorkerBuilder setRadius(double radius) {
        this.radius = radius;
        return this;
    }
    public WorkerBuilder setSpeed(int speed) {
        this.speed = speed;
        return this;
    }
    public WorkerBuilder setDps(int dps) {
        this.dps = dps;
        return this;
    }
    public WorkerBuilder setWanderState(boolean wanderState) {
        this.wanderState = wanderState;
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
    public boolean getZone() {
        return this.zone;
    }
    public double getRadius() {
        return this.radius;
    }
    public int getSpeed() {
        return this.speed;
    }
    public int getDps() {
        return this.dps;
    }
    public boolean getWanderState() {
        return this.wanderState;
    }
}