package application;

import game.GM;

public class EngineCreator extends Thread {
    public static VisualEngine gcEngine;
    
    public void run() {
        gcEngine = new VisualEngine(new GM());
    }

}
