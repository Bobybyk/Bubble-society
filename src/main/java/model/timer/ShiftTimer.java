package model.timer;

import java.util.TimerTask;

import model.GameMap;

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
