package model.timer;

import java.util.TimerTask;

public class ShiftTimer extends TimerTask {

    @Override
    public void run() {
        System.out.println("SHIFT");
    }
    
}
