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
package game.worker;

public abstract class Worker {

    protected int hp; // [25 ; 50] état initial
    protected int will_power; // [10 ; 20] - état initial
    protected boolean zone;
    protected double radius;
    protected int speed;
    protected int dps;
    protected boolean wanderState;
    protected boolean lifeState;
    protected int zoneId;

    public Worker (WorkerBuilder worker) {
        this.hp = worker.getHp();
        this.will_power = worker.getWill();
        this.zone = worker.getZone();
        this.radius = worker.getRadius();
        this.speed = worker.getSpeed();
        this.dps = worker.getDps();
        this.wanderState = worker.getWanderState();
        this.lifeState = worker.getLifeState();
        this.zoneId = worker.getZoneId();
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
    public boolean getLifeState() {
        return this.lifeState;
    }
    public int getZoneId() {
        return this.zoneId;
    }

    public void decreaseHp() {
        this.hp--;
    }
    public void increaseHp() {
        this.hp++;
    }
    public boolean isInsurgent() {
        if (this instanceof Insurgent) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isFollower() {
        if (this instanceof Follower) {
            return true;
        } else {
            return false;
        }
    }
    public void setWanderState(boolean state) {
        wanderState = state;
    }
    public void setLifeState(boolean state) {
        lifeState = state;
    }
    public void setZoneId(int id) {
        this.zoneId = id;
    }

}
