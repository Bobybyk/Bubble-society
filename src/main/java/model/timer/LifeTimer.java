package model.timer;

import java.util.TimerTask;

import controller.World;

public class LifeTimer extends TimerTask {
    private World world;

    public LifeTimer(World world) {
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
