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
package application.system.timers;

import game.Game;

import static org.lwjgl.glfw.GLFW.*;


public class GameTimer {
	private int lastTimeProccessed = 0;
	private int timeProccessed = 0;

    private Game game;
    

    public GameTimer(Game game) {
        this.game = game;
    }

    public void gameProcess() {
		timeProccessed = (int)glfwGetTime();
		if (timeProccessed%2==0 && timeProccessed != lastTimeProccessed) {
			game.multiSpawn();
			lastTimeProccessed = timeProccessed;
		}
		if (timeProccessed%1==0 && timeProccessed != lastTimeProccessed) {
			game.decreaseWorkersHp();
			lastTimeProccessed = timeProccessed;
		}
	}

} 
