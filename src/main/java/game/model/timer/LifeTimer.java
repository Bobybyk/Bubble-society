package game.model.timer;

import java.util.TimerTask;

import game.GM;

public class LifeTimer extends TimerTask {
    private GM world;

    public LifeTimer(GM world) {
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
