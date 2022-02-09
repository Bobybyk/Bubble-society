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

import game.model.GameMap;

public class ShiftTimer extends TimerTask {
    private GameMap map;

    public ShiftTimer(GameMap map) {
        this.map = map;
    }

    @Override
    public void run() {
        map.wander();
    }
    
}
