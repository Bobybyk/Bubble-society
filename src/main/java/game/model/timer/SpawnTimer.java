package game.model.timer;

import java.util.TimerTask;

import application.debug.DebugLogger;
import game.GM;

public class SpawnTimer extends TimerTask {
	private GM world;

	int counter = 10;

	public SpawnTimer(GM world) {
		this.world = world;
	}

	@Override
	public void run() {
		// DEBBUG
		if (DebugLogger.debug) System.out.println("SPAWN");
		world.multiSpawn();
		world.testMeeting();
		counter--;
		if (counter < 1) {
			cancel();
		}
	}
}
