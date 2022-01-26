package game.model.timer;

import java.util.TimerTask;

import application.DevMode;
import game.World;

public class SpawnTimer extends TimerTask {
	private World world;

	int counter = 10;

	public SpawnTimer(World world) {
		this.world = world;
	}

	@Override
	public void run() {
		// DEBBUG
		if (DevMode.debug) System.out.println("SPAWN");
		world.multiSpawn();
		world.testMeeting();
		counter--;
		if (counter < 1) {
			cancel();
		}
	}
}
