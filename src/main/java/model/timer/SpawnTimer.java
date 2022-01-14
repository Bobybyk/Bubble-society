package model.timer;

import java.util.TimerTask;

import controller.World;

public class SpawnTimer extends TimerTask {
	int currentTime = 0; // 49 jours avant bug
	int currentTimeSec = 0;

	private World world;

	public SpawnTimer(World world) {
		this.world = world;
	}

	@Override
	public void run() {
		currentTimeSec+=1;

		if (currentTimeSec%5 == 0) {
			System.out.println("SPAWN");
			world.multiSpawn();
		}

	}

	public int getCurrentTime() {
		return currentTime;
	}

	public int getCurrentTimeSec() {
		return currentTimeSec;
	}
}
