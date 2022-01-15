package model.timer;

import java.util.TimerTask;

import controller.World;

public class SpawnTimer extends TimerTask {
	private World world;

	int counter = 10;

	public SpawnTimer(World world) {
		this.world = world;
	}

	@Override
	public void run() {
		// DEBBUG
		System.out.println("SPAWN");
		world.multiSpawn();
		counter--;
		if (counter < 1) {
			cancel();
		}
	}
}
