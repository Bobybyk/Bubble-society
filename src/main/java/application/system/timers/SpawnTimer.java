package application.system.timers;

import game.Game;

import static org.lwjgl.glfw.GLFW.*;


public class SpawnTimer {
	private int lastTimeProccessed = 0;
	private int timeProccessed = 0;

    private Game game;
    

    public SpawnTimer(Game game) {
        this.game = game;
    }

    public void gameProcess() {
		timeProccessed = (int)glfwGetTime();
		if (timeProccessed%5==0 && timeProccessed != lastTimeProccessed) {
			game.multiSpawn();
			lastTimeProccessed = timeProccessed;
			//System.out.println(timeProccessed);
			//System.out.println(lastTimeProccessed);
		}
	}

} 
