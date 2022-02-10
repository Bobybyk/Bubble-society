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

import application.debug.DebugLogger;
import application.debug.DebugType;
import game.Game;

public class SpawnTimer extends TimerTask {
	private Game world;

	int counter = 10;

	public SpawnTimer(Game world) {
		this.world = world;
	}

	@Override
	public void run() {
		// DEBBUG
		DebugLogger.print(DebugType.ALL, "SPAWN");
		world.multiSpawn();
		world.testMeeting();
		counter--;
		if (counter < 1) {
			cancel();
		}
	}
}
