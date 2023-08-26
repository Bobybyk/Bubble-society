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

    public Worker(WorkerBuilder worker) {
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

    /**
     * @return current hp number
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * @return current will power value
     */
    public int getWill() {
        return this.will_power;
    }

    /**
     * @return is or not in a zone
     */
    public boolean getZone() {
        return this.zone;
    }

    /**
     * @return radius val
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * @return max speed val
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * @return dps val
     */
    public int getDps() {
        return this.dps;
    }

    /**
     * @return is wandering or on a target
     */
    public boolean getWanderState() {
        return this.wanderState;
    }

    /**
     * @return is alive or dead
     */
    public boolean getLifeState() {
        return this.lifeState;
    }

    /**
     * @return zone id
     */
    public int getZoneId() {
        return this.zoneId;
    }

    /** decrease hp by 1 */
    public void decreaseHp() {
        this.hp--;
    }

    /** increase hp by 1 */
    public void increaseHp() {
        this.hp++;
    }

    /**
     * @return true if insurgent, false if follower
     */
    public boolean isInsurgent() {
        if (this instanceof Insurgent) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if follower, false if insurgent
     */
    public boolean isFollower() {
        if (this instanceof Follower) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param state new state
     */
    public void setWanderState(boolean state) {
        wanderState = state;
    }

    /**
     * @param state new state
     */
    public void setLifeState(boolean state) {
        lifeState = state;
    }

    /**
     * @param id new id
     */
    public void setZoneId(int id) {
        this.zoneId = id;
    }
}
