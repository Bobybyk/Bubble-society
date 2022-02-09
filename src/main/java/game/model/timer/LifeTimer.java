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
package game.model.timer;

import java.util.TimerTask;

import game.Game;

public class LifeTimer extends TimerTask {
    private Game world;

    public LifeTimer(Game world) {
        this.world = world;
    }

    @Override
	public void run() {
        // if decreaseHPForMap() return false, every workers are dead
        if(world.decreaseHpForMap() == false) {
            cancel();
        }
        
	}
}
